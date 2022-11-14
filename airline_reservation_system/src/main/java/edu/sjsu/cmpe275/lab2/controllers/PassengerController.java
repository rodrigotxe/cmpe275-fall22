package edu.sjsu.cmpe275.lab2.controllers;

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

import edu.sjsu.cmpe275.lab2.entities.Passenger;
import edu.sjsu.cmpe275.lab2.entities.Reservation;
import edu.sjsu.cmpe275.lab2.services.FlightService;
import edu.sjsu.cmpe275.lab2.services.PassengerService;
import edu.sjsu.cmpe275.lab2.services.ReservationService;
import edu.sjsu.cmpe275.lab2.util.ResponseUtil;

@RestController
@CrossOrigin(origins = "*")
public class PassengerController {

	private static final Logger LOG = LoggerFactory.getLogger(PassengerController.class);

	@Autowired
	private PassengerService passengerService;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private FlightService flightService;

	/**
	 * Returns passenger object if given id is valid. Else, custom error response is returned based on validation failure.
	 * @param id identifier of passenger
	 * @param xml desired output format/view. If true, format is XML else format is JSON.
	 * @return
	 */
	@RequestMapping(value = "/passenger/{id}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> getPassenger(@PathVariable("id") String id, @RequestParam("xml") String xml) {
		
		LOG.info("Executing getPassenger() << {}, {}", id, xml);

		boolean xmlView = "true".equals(xml);

		HttpHeaders headers = new HttpHeaders();

		if (xmlView)
			headers.setContentType(MediaType.APPLICATION_XML);

		Passenger passenger = passengerService.getPassenger(id);

		if (passenger == null) {
			LOG.error("Passenger cannot be found with ID : {}", id);

			return ResponseUtil.customResponse("404",
					"Sorry, the requested passenger with ID " + id + " does not exist", ResponseUtil.BAD_REQUEST,
					xmlView, headers, HttpStatus.NOT_FOUND);
		}

		LOG.info("Exiting getPassenger() >> {}", id);

		return new ResponseEntity<Passenger>(passenger, headers, HttpStatus.OK);

	}

	/**
	 * Returns newly created passenger object if all validations are pass. Else, custom error response is returned based on validation failure.
	 * @param firstName first name of passenger
	 * @param lastName last name of passenger
	 * @param birthYear year of birth of passenger
	 * @param gender gender of passenger
	 * @param phone phone number of passenger
	 * @param xml desired output format/view. If true, format is XML else format is JSON.
	 * @return
	 */
	@RequestMapping(value = "/passenger", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> createPassenger(@RequestParam("firstname") String firstName,
			@RequestParam("lastname") String lastName, @RequestParam("birthyear") int birthYear,
			@RequestParam("gender") String gender, @RequestParam("phone") String phone,
			@RequestParam("xml") String xml) {
		
		LOG.info("Executing createPassenger() << {}, {}, {}, {}, {}, {}", firstName, lastName, birthYear, gender, phone,
				xml);

		boolean xmlView = "true".equals(xml);

		HttpHeaders headers = new HttpHeaders();

		if (xmlView)
			headers.setContentType(MediaType.APPLICATION_XML);

		Passenger passenger = passengerService.findByPhone(phone);

		if (passenger != null) {
			LOG.error("Another passenger already exists with phone : {}", phone);
			
			return ResponseUtil.customResponse("400",
					"Another passenger with the same number " + phone + " already exists", ResponseUtil.BAD_REQUEST,
					xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		String uuid = UUID.randomUUID().toString();
		
		Passenger newPassenger = new Passenger();

		newPassenger.setId(uuid);
		newPassenger.setFirstName(firstName);
		newPassenger.setLastName(lastName);
		newPassenger.setBirthYear(birthYear);
		newPassenger.setGender(gender);
		newPassenger.setPhone(phone);

		Passenger createdPassenger = passengerService.addPassenger(newPassenger);
		
		LOG.info("Exiting createPassenger() >> {}", uuid);

		return new ResponseEntity<Passenger>(createdPassenger, headers, HttpStatus.OK);

	}

	/**
	 * Updates passenger object with all given information. Else, custom error response is returned based on validation failure.
	 * @param id identifier of passenger
	 * @param firstName first name of passenger
	 * @param lastName last name of passenger
	 * @param birthYear year of birth of passenger
	 * @param gender gender of passenger
	 * @param phone phone number of passenger
	 * @param xml desired output format/view. If true, format is XML else format is JSON
	 * @return
	 */
	@RequestMapping(value = "/passenger/{id}", method = RequestMethod.PUT, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> updatePassenger(@PathVariable("id") String id, @RequestParam("firstname") String firstName,
			@RequestParam("lastname") String lastName, @RequestParam("birthyear") int birthYear,
			@RequestParam("gender") String gender, @RequestParam("phone") String phone,
			@RequestParam("xml") String xml) {
		
		LOG.info("Executing updatePassenger() << {}, {}", id, xml);

		boolean xmlView = "true".equals(xml);

		HttpHeaders headers = new HttpHeaders();

		if (xmlView)
			headers.setContentType(MediaType.APPLICATION_XML);
		
		Passenger passenger = passengerService.getPassenger(id);

		if (passenger == null) {
			LOG.error("Passenger cannot be found with ID : {}", id);
			
			return ResponseUtil.customResponse("404",
					"Sorry, the requested passenger with ID " + id + " does not exist", ResponseUtil.BAD_REQUEST,
					xmlView, headers, HttpStatus.NOT_FOUND);
		}

		passenger.setFirstName(firstName);
		passenger.setLastName(lastName);
		passenger.setBirthYear(birthYear);
		passenger.setGender(gender);
		passenger.setPhone(phone);

		Passenger updatedPassenger = passengerService.updatePassenger(passenger);
		
		LOG.info("Exiting updatePassenger() >> {}", id);

		return new ResponseEntity<Passenger>(updatedPassenger, headers, HttpStatus.OK);

	}

	/**
	 * Returns 200 success response on successful deletion of passenger. Else, custom error response is returned based on validation failure. 
	 * @param id identifier of passenger
	 * @param xml desired output format/view. If true, format is XML else format is JSON
	 * @return
	 */
	@RequestMapping(value = "/passenger/{id}", method = RequestMethod.DELETE, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> deletePassenger(@PathVariable("id") String id, @RequestParam("xml") String xml) {
		
		LOG.info("Executing deletePassenger() << {}, {}", id, xml);

		Passenger passenger = passengerService.getPassenger(id);

		boolean xmlView = "true".equals(xml);

		HttpHeaders headers = new HttpHeaders();

		if (xmlView)
			headers.setContentType(MediaType.APPLICATION_XML);

		if (passenger == null) {
			LOG.error("Passenger cannot be found with ID : {}", id);
			
			return ResponseUtil.customResponse("404", "Passenger with ID " + id + " does not exist",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.NOT_FOUND);
		}

		for (Reservation reservation : passenger.getReservations()) {

			reservationService.cancelReservation(reservation.getReservationNumber());

			flightService.updateSeats(reservation.getFlights(), false);

		}

		passengerService.deletePassenger(id);
		
		LOG.info("Exiting deletePassenger() >> {}", id);

		return ResponseUtil.customResponse("200", "Passenger with ID " + id + " deleted successfully",
				ResponseUtil.SUCCESS, xmlView, headers, HttpStatus.OK);

	}

}
