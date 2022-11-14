package edu.sjsu.cmpe275.lab2.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.cmpe275.lab2.entities.Flight;
import edu.sjsu.cmpe275.lab2.entities.FlightKey;
import edu.sjsu.cmpe275.lab2.entities.Plane;
import edu.sjsu.cmpe275.lab2.services.FlightService;
import edu.sjsu.cmpe275.lab2.util.ResponseUtil;

@RestController
@CrossOrigin(origins = "*")
public class FlightController {

	private static final Logger LOG = LoggerFactory.getLogger(FlightController.class);

	@Autowired
	private FlightService flightService;

	/**
	 * returns the Flight object from if given id is valid. Else, custom error response is returned based on validation failure.
	 * @param flightNumber flight number of flight
	 * @param departureDateS departure date of flight
	 * @param xml desired output format/view. If true, format is XML else format is JSON.
	 * @return
	 */
	@RequestMapping(value = "/flight/{flightNumber}/{departureDate}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> getFlight(@PathVariable("flightNumber") String flightNumber,
			@PathVariable("departureDate") String departureDateS, @RequestParam("xml") String xml) {

		LOG.info("Executing getFlight() << {}, {}, {}", flightNumber, departureDateS, xml);

		boolean xmlView = "true".equals(xml);

		HttpHeaders headers = new HttpHeaders();

		if (xmlView)
			headers.setContentType(MediaType.APPLICATION_XML);

		Date departureDate;

		try {
			departureDate = new SimpleDateFormat("yyyy-MM-dd").parse(departureDateS);
		} catch (ParseException e) {
			LOG.error("Error parsing departure dates {}", departureDateS);

			return ResponseUtil.customResponse("404", e.getMessage(), ResponseUtil.BAD_REQUEST, xmlView, headers,
					HttpStatus.NOT_FOUND);
		}

		Flight flight = flightService.getFlight(flightNumber, departureDate);

		if (flight == null) {
			LOG.error("No flight exists with ID : {}, {}", flightNumber, departureDate);

			return ResponseUtil.customResponse("404",
					"Sorry, the requested flight with number " + flightNumber + " does not exist",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.NOT_FOUND);

		}

		LOG.info("Exiting getFlight() >> {}, {}, {}", flightNumber, departureDateS, xml);

		return new ResponseEntity<Flight>(flight, headers, HttpStatus.OK);

	}

	/**
	 * Returns newly created or updated flight object if all validations pass. Else, custom error response is returned based on validation failure.
	 * @param flightNumber flight number of flight
	 * @param departureDateS departure date of flight
	 * @param departureTimeS departure time of flight
	 * @param arrivalTimeS arrival time of flight
	 * @param price fare of flight
	 * @param origin origin of flight
	 * @param destination destination of flight
	 * @param description description of flight
	 * @param capacity capacity of flight
	 * @param model model of flight
	 * @param manufacturer manufacturer of flight
	 * @param yearOfManufacture year of manufacture of flight
	 * @param xml desired output format/view. If true, format is XML else format is JSON.
	 * @return
	 */
	@RequestMapping(value = "/flight/{flightNumber}/{departureDate}", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> createFlight(@PathVariable("flightNumber") String flightNumber,
			@PathVariable("departureDate") String departureDateS, @RequestParam("departureTime") String departureTimeS,
			@RequestParam("arrivalTime") String arrivalTimeS, @RequestParam("price") int price,
			@RequestParam("origin") String origin, @RequestParam("destination") String destination,
			@RequestParam("description") String description, @RequestParam("capacity") int capacity,
			@RequestParam("model") String model, @RequestParam("manufacturer") String manufacturer,
			@RequestParam("yearOfManufacture") int yearOfManufacture, @RequestParam("xml") String xml) {

		LOG.info("Executing createFlight() << {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}", flightNumber,
				departureDateS, departureTimeS, arrivalTimeS, price, origin, destination, description, capacity, model,
				manufacturer, yearOfManufacture, xml);

		boolean xmlView = "true".equals(xml);

		HttpHeaders headers = new HttpHeaders();

		if (xmlView)
			headers.setContentType(MediaType.APPLICATION_XML);

		// Check if the departureTime and departureDate are on the same day
		if (!departureDateS.equals(departureTimeS.substring(0, 10))) {
			LOG.error("Departure Time : {} and departure Date : {} are not on the same day", departureTimeS,
					departureDateS);

			return ResponseUtil.customResponse("400", "Departure Time and departure Date are not on the same day",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.NOT_FOUND);
		}

		Date departureDate;
		Date departureTime;
		Date arrivalTime;

		try {
			departureDate = new SimpleDateFormat("yyyy-MM-dd").parse(departureDateS);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH");
			departureTime = simpleDateFormat.parse(departureTimeS);
			arrivalTime = simpleDateFormat.parse(arrivalTimeS);
		} catch (ParseException e) {
			LOG.error("Error parsing departure date {}, departure time {}, arrivaltime {}", departureDateS,
					departureTimeS, arrivalTimeS);

			return ResponseUtil.customResponse("404", e.getMessage(), ResponseUtil.BAD_REQUEST, xmlView, headers,
					HttpStatus.NOT_FOUND);
		}

		Flight flight = flightService.getFlight(flightNumber, departureDate);

		if (flight == null) {
			flight = new Flight();
			flight.setFlightKey(new FlightKey(flightNumber, departureDate));
			flight.setSeatsLeft(capacity);
			flight.setDepartureTime(departureTime);
			flight.setArrivalTime(arrivalTime);
		} else {
			if (capacity != flight.getPlane().getCapacity()) {
				if (flight.getReservations().size() > capacity) {
					LOG.error("Flight capacity is full");

					return ResponseUtil.customResponse("400",
							"Active reservation count is higher than the target capacity", ResponseUtil.BAD_REQUEST,
							xmlView, headers, HttpStatus.NOT_FOUND);
				}

				flight.setSeatsLeft(capacity - flight.getReservations().size());
			}

			if (flight.getDepartureTime() != departureTime || flight.getArrivalTime() != arrivalTime) {
				flight.setDepartureTime(departureTime);
				flight.setArrivalTime(arrivalTime);

				if (flightService.hasFlightConflict(flight)) {
					LOG.error("Flights has timing conflicts");

					return ResponseUtil.customResponse("400",
							"New departure/arrival time is causing overlapping with at least one passenger",
							ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.NOT_FOUND);
				}
			}
		}

		flight.setPrice(price);
		flight.setOrigin(origin);
		flight.setDestination(destination);

		Plane plane = new Plane(model, capacity, manufacturer, yearOfManufacture);

		flight.setPlane(plane);

		Flight updatedFlight = flightService.addUpdateFlight(flight);

		LOG.info("Exiting createFlight() >> {}, {}", flightNumber, departureDateS);

		return new ResponseEntity<Flight>(updatedFlight, headers, HttpStatus.OK);
	}

	/**
	 * Returns 200 success response on successful deletion of flight. Else, custom error response is returned based on validation failure.
	 * @param flightNumber flight number of flight
	 * @param departureDateS departure date of flight
	 * @param xml desired output format/view. If true, format is XML else format is JSON.
	 * @return
	 */
	@RequestMapping(value = "/flight/{flightNumber}/{departureDate}", method = RequestMethod.DELETE, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> deleteFlight(@PathVariable("flightNumber") String flightNumber,
			@PathVariable("departureDate") String departureDateS, @RequestParam("xml") String xml) {

		LOG.info("Executing deleteFlight() << {}, {}, {}", flightNumber, departureDateS, xml);

		boolean xmlView = "xml".equals(xml);

		HttpHeaders headers = new HttpHeaders();

		if (xmlView)
			headers.setContentType(MediaType.APPLICATION_XML);

		Date departureDate;

		try {
			departureDate = new SimpleDateFormat("yyyy-MM-dd").parse(departureDateS);
		} catch (ParseException e) {
			LOG.error("Error parsing departure dates {}", departureDateS);

			return ResponseUtil.customResponse("404", e.getMessage(), ResponseUtil.BAD_REQUEST, xmlView, headers,
					HttpStatus.NOT_FOUND);
		}

		Flight flight = flightService.getFlight(flightNumber, departureDate);

		if (flight == null) {
			LOG.error("No flight exists with ID : {}, {}", flightNumber, departureDate);

			return ResponseUtil.customResponse("404",
					"Flight with number " + flightNumber + " and departure date " + departureDate + " does not exist",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.NOT_FOUND);
		} else if (flight.getReservations().size() > 0) {
			LOG.error("Flight reached it's full capacity of reservations");

			return ResponseUtil.customResponse("400", "This flight has active reservations", ResponseUtil.BAD_REQUEST,
					xmlView, headers, HttpStatus.NOT_FOUND);
		}

		flightService.deleteFlight(flightNumber, departureDate);

		LOG.info("Exiting deleteFlight() >> {}, {}", flightNumber, departureDateS);

		return ResponseUtil.customResponse("200", "Flight with number " + flightNumber + " deleted successfully",
				ResponseUtil.SUCCESS, xmlView, headers, HttpStatus.OK);
	}

}