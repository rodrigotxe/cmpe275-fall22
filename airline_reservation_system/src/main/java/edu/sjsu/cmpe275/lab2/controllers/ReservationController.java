package edu.sjsu.cmpe275.lab2.controllers;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
import edu.sjsu.cmpe275.lab2.entities.Passenger;
import edu.sjsu.cmpe275.lab2.entities.Reservation;
import edu.sjsu.cmpe275.lab2.services.FlightService;
import edu.sjsu.cmpe275.lab2.services.PassengerService;
import edu.sjsu.cmpe275.lab2.services.ReservationService;
import edu.sjsu.cmpe275.lab2.util.ResponseUtil;

@RestController
@CrossOrigin(origins = "*")
public class ReservationController {

	private static final Logger LOG = LoggerFactory.getLogger(ReservationController.class);

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private PassengerService passengerService;

	@Autowired
	private FlightService flightService;

	@RequestMapping(value = "/reservation/{number}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> getReservation(@PathVariable("number") String reservationNumber,
			@RequestParam("xml") String xml) {
		Reservation reservation = reservationService.getReservation(reservationNumber);

		boolean xmlView = "true".equals(xml);

		HttpHeaders headers = new HttpHeaders();

		if (xmlView)
			headers.setContentType(MediaType.APPLICATION_XML);

		if (reservation == null) {
			return ResponseUtil.customResponse("404",
					"Reservation with number " + reservationNumber + " does not exist", ResponseUtil.BAD_REQUEST,
					xmlView, headers, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Reservation>(reservation, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/reservation", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> makeReservation(@RequestParam("passengerId") String passengerId,
			@RequestParam("flightNumbers") String flightNumbers, @RequestParam("departureDates") String departureDates,
			@RequestParam("xml") String xml) {

		boolean xmlView = "xml".equals(xml);

		HttpHeaders headers = new HttpHeaders();

		if (xmlView)
			headers.setContentType(MediaType.APPLICATION_XML);

		// 1. check for passenger existence
		Passenger passenger = passengerService.getPassenger(passengerId);

		if (passenger == null) {
			return ResponseUtil.customResponse("400",
					"Reservation cannot be made as passenger with the ID" + passengerId + "does not exist",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		Date[] dateOfDepartures;

		// 2. parse given dates
		try {
			dateOfDepartures = reservationService.parse(departureDates);
		} catch (ParseException ex) {
			return ResponseUtil.customResponse("400", "Please provide all dates in YYYY-MM-DD format",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		String[] flightIds = flightNumbers.split(",");

		// 3. check for flights available
		List<Flight> flights = flightService.getFlights(flightIds, dateOfDepartures);

		if (flights.isEmpty()) {
			return ResponseUtil.customResponse("400", "There are no flights with given criteria",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		// 4. check for time conflicts among given flights
		boolean isTimeconflict = flightService.isTimeConflicts(flights);

		if (isTimeconflict) {
			return ResponseUtil.customResponse("400",
					"Reservation cannot be made due to time conflicts for selected flights. To proceed for Reservation, Please select different flights which has no time conflicts",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		// 5. check for capacity
		int index = flightService.getIndexOfFlightHavingFullCapacity(flights);

		if (index != -1) {
			return ResponseUtil.customResponse("400",
					"Flight capacity is full for flight number : " + flights.get(index).getFlightKey().getFlightNumber()
							+ " with departure date : " + flights.get(index).getFlightKey().getDepartureDate()
							+ ". Please choose a different available flight to proceed further",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		List<Reservation> reservations = passenger.getReservations();

		// 6. check for time conflict with existing reservations
		String reservationNumber = reservationService.getReservationConflictNumber(flights, reservations);

		if (reservationNumber != null) {
			return ResponseUtil.customResponse("400",
					"Conflict with the existing reservation number : " + reservationNumber
							+ ". Please choose a different available flight",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		// if every check is passed, then create a reservation object and save the
		// entity to database
		String origin = flights.get(0).getOrigin();
		String destination = flights.get(flights.size() - 1).getDestination();
		int price = flightService.getPrice(flights);

		String uuid = UUID.randomUUID().toString();

		Reservation reservation = new Reservation();
		reservation.setReservationNumber(uuid);
		reservation.setOrigin(origin);
		reservation.setDestination(destination);
		reservation.setPassenger(passenger);
		reservation.setFlights(flights);
		reservation.setPrice(price);

		System.out.println(uuid);

		Reservation createdReservation = reservationService.makeReservation(reservation);

		flightService.updateSeats(flights, true);

//		flightService.addPassengerToFlights(flights, passenger);

		// update passenger with flights after making reservation
		passengerService.updatePassengerWithFlights(passenger, flights);

		return new ResponseEntity<Reservation>(createdReservation, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/reservation/{number}", method = RequestMethod.PUT, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> updateReservation(@PathVariable("number") String reservationNumber,
			@RequestParam("flightsAdded") String flightsAdded,
			@RequestParam("departureDatesAdded") String departureDatesAdded,
			@RequestParam("flightsRemoved") String flightsRemoved,
			@RequestParam("departureDatesRemoved") String departureDatesRemoved, @RequestParam("xml") String xml) {

		boolean xmlView = "true".equals(xml);

		HttpHeaders headers = new HttpHeaders();

		if (xmlView)
			headers.setContentType(MediaType.APPLICATION_XML);

		Reservation reservation = reservationService.getReservation(reservationNumber);

		if (reservation == null) {
			return ResponseUtil.customResponse("404", "Reservation with ID : " + reservationNumber + " does not exist",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.NOT_FOUND);
		}

		Date[] departureDatesForFlightsAdded;
		Date[] departureDatesForFlightsRemoved;

		// parsing departureDatesAdded
		try {
			departureDatesForFlightsAdded = reservationService.parse(departureDatesAdded);
			departureDatesForFlightsRemoved = reservationService.parse(departureDatesRemoved);
		} catch (ParseException ex) {
			return ResponseUtil.customResponse("400", "Please provide all dates in YYYY-MM-DD format",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		String[] flightNumbersAdded = flightsAdded.split(",");
		String[] flightNumbersRemoved = flightsRemoved.split(",");

		List<Flight> flightsRemovedList = flightService.getFlights(flightNumbersRemoved,
				departureDatesForFlightsRemoved);

		if (flightsRemovedList.size() == 0) {
			return ResponseUtil.customResponse("400",
					"There are no flights for flights removed with their respective departure dates",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		List<Flight> flightsAddedList = flightService.getFlights(flightNumbersAdded, departureDatesForFlightsAdded);

		if (flightsAddedList.size() == 0) {
			return ResponseUtil.customResponse("400",
					"There are no flights for flights added with their respective departure dates",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		boolean isFlightsExist = reservationService.isFlightsExist(reservation, flightsRemovedList);

		List<Flight> reservedFlights = reservation.getFlights();

		if (isFlightsExist) {
			reservedFlights = reservationService.removeFlightsFromReservation(reservation, flightsRemovedList);
		}

		reservedFlights.addAll(flightsAddedList);
		reservedFlights.sort((a, b) -> a.getDepartureTime().compareTo(b.getDepartureTime()));

		boolean isTimeConflicts = flightService.isTimeConflicts(reservedFlights);

		if (isTimeConflicts) {
			return ResponseUtil.customResponse("400",
					"Reservation cannot be updated due to time conflicts with flights added", ResponseUtil.BAD_REQUEST,
					xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		String origin = reservedFlights.get(0).getOrigin();
		String destination = reservedFlights.get(reservedFlights.size() - 1).getDestination();
		int price = flightService.getPrice(reservedFlights);

		reservation.setOrigin(origin);
		reservation.setDestination(destination);
		reservation.setPrice(price);

		Reservation updatedReservation = reservationService.updateReservation(reservation);

		flightService.updateSeats(flightsAddedList, true);
		flightService.updateSeats(flightsRemovedList, false);

		passengerService.updatePassengerWithFlights(reservation.getPassenger(), reservedFlights);

		return new ResponseEntity<Reservation>(updatedReservation, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/reservation/{number}", method = RequestMethod.DELETE, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> cancelReservation(@PathVariable("number") String reservationNumber,
			@RequestParam("xml") String xml) {

		boolean xmlView = xml.equals("true");

		HttpHeaders headers = new HttpHeaders();

		if (xmlView)

			headers.setContentType(MediaType.APPLICATION_XML);

		Reservation reservation = reservationService.getReservation(reservationNumber);

		if (reservation == null) {

			return ResponseUtil.customResponse("404",
					"Reservation with number " + reservationNumber + " does not exist", ResponseUtil.BAD_REQUEST,
					xmlView, headers, HttpStatus.NOT_FOUND);

		}

		reservationService.cancelReservation(reservationNumber);

		flightService.updateSeats(reservation.getFlights(), false);

		return ResponseUtil.customResponse("200",
				"Reservation with number " + reservationNumber + " is canceled successfully ", ResponseUtil.SUCCESS,
				xmlView, headers, HttpStatus.OK);

	}

}