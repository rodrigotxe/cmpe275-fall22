package edu.sjsu.cmpe275.lab2.controllers;

import java.util.UUID;

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
import edu.sjsu.cmpe275.lab2.services.PassengerService;
import edu.sjsu.cmpe275.lab2.util.ResponseUtil;

@RestController
@CrossOrigin(origins = "*")
public class PassengerController {

	@Autowired
	private PassengerService passengerService;

	@RequestMapping(value = "/passenger/{id}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> getPassenger(@PathVariable("id") String id, @RequestParam("xml") String xml) {

		Passenger passenger = passengerService.getPassenger(id);

		boolean xmlView = "true".equals(xml);

		HttpHeaders headers = new HttpHeaders();

		if (xmlView)
			headers.setContentType(MediaType.APPLICATION_XML);

		if (passenger == null) {
			return ResponseUtil.customResponse("404",
					"Sorry, the requested passenger with ID " + id + " does not exist", ResponseUtil.BAD_REQUEST,
					xmlView, headers, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Passenger>(passenger, headers, HttpStatus.OK);

	}

	@RequestMapping(value = "/passenger", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> createPassenger(@RequestParam("firstname") String firstName,
			@RequestParam("lastname") String lastName, @RequestParam("birthyear") int birthYear,
			@RequestParam("gender") String gender, @RequestParam("phone") String phone,
			@RequestParam("xml") String xml) {

		Passenger passenger = passengerService.findByPhone(phone);

		boolean xmlView = "true".equals(xml);

		HttpHeaders headers = new HttpHeaders();

		if (xmlView)
			headers.setContentType(MediaType.APPLICATION_XML);

		if (passenger != null) {
			return ResponseUtil.customResponse("400",
					"Another passenger with the same number " + phone + " already exists", ResponseUtil.BAD_REQUEST,
					xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		Passenger newPassenger = new Passenger();

		newPassenger.setId(UUID.randomUUID().toString());
		newPassenger.setFirstName(firstName);
		newPassenger.setLastName(lastName);
		newPassenger.setBirthYear(birthYear);
		newPassenger.setGender(gender);
		newPassenger.setPhone(phone);

		Passenger createdPassenger = passengerService.addPassenger(newPassenger);

		return new ResponseEntity<Passenger>(createdPassenger, headers, HttpStatus.OK);

	}

	@RequestMapping(value = "/passenger/{id}", method = RequestMethod.PUT, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> updatePassenger(@PathVariable("id") String id, @RequestParam("firstname") String firstName,
			@RequestParam("lastname") String lastName, @RequestParam("birthyear") int birthYear,
			@RequestParam("gender") String gender, @RequestParam("phone") String phone,
			@RequestParam("xml") String xml) {

		Passenger passenger = passengerService.getPassenger(id);

		boolean xmlView = "true".equals(xml);

		HttpHeaders headers = new HttpHeaders();

		if (xmlView)
			headers.setContentType(MediaType.APPLICATION_XML);

		if (passenger == null) {
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

		return new ResponseEntity<Passenger>(updatedPassenger, headers, HttpStatus.OK);

	}

	@RequestMapping(value = "/passenger/{id}", method = RequestMethod.DELETE, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> deletePassenger(@PathVariable("id") String id, @RequestParam("xml") String xml) {

		Passenger passenger = passengerService.getPassenger(id);

		boolean xmlView = "true".equals(xml);

		HttpHeaders headers = new HttpHeaders();

		if (xmlView)
			headers.setContentType(MediaType.APPLICATION_XML);

		if (passenger == null) {
			return ResponseUtil.customResponse("404", "Passenger with ID " + id + " does not exist",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.NOT_FOUND);
		}

		passengerService.deletePassenger(id);

		return ResponseUtil.customResponse("200", "Passenger with ID " + id + " deleted successfully",
				ResponseUtil.SUCCESS, xmlView, headers, HttpStatus.OK);

	}

}
