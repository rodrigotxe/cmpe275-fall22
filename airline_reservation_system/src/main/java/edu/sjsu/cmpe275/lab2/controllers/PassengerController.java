package edu.sjsu.cmpe275.lab2.controllers;

import java.util.HashMap;

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
import edu.sjsu.cmpe275.lab2.response.Response;
import edu.sjsu.cmpe275.lab2.services.PassengerService;

@RestController
@CrossOrigin(origins = "*")
public class PassengerController {

	@Autowired
	private PassengerService passengerService;
	
	private static final String BAD_REQUEST = "BadRequest";
	private static final String SUCCESS = "Success";

	@RequestMapping(value = "/passenger/{id}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> getPassenger(@PathVariable("id") String id, @RequestParam("xml") String xml) {
		Passenger passenger = passengerService.getPassenger(id);

		HttpHeaders headers = new HttpHeaders();

		if ("true".equals(xml))
			headers.setContentType(MediaType.APPLICATION_XML);

		if (passenger == null) {
			Response response = new Response();
			response.setCode("404");
			response.setMsg("Sorry, the requested passenger with ID " + id + " does not exist");

			if ("true".equals(xml)) {
				return new ResponseEntity<Response>(response, headers, HttpStatus.NOT_FOUND);
			}

			HashMap<String, Response> map = new HashMap<>();
			map.put(BAD_REQUEST, response);
			return new ResponseEntity<>(map, headers, HttpStatus.NOT_FOUND);
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
		
		HttpHeaders headers = new HttpHeaders();

		if ("true".equals(xml))
			headers.setContentType(MediaType.APPLICATION_XML);
		
		if (passenger != null) {
			Response response = new Response();
			response.setCode("400");
			response.setMsg("Another passenger with the same number " + phone + " already exists");
			
			if ("true".equals(xml)) {
				return new ResponseEntity<Response>(response, headers, HttpStatus.BAD_REQUEST);
			}
			
			HashMap<String, Response> map = new HashMap<>();
			map.put(BAD_REQUEST, response);
			return new ResponseEntity<>(map, headers, HttpStatus.BAD_REQUEST);
		}
		
		Passenger newPassenger = new Passenger();

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
		
		HttpHeaders headers = new HttpHeaders();

		if ("true".equals(xml))
			headers.setContentType(MediaType.APPLICATION_XML);

		if (passenger == null) {
			Response response = new Response();
			response.setCode("404");
			response.setMsg("Sorry, the requested passenger with ID " + id + " does not exist");

			if ("true".equals(xml)) {
				return new ResponseEntity<Response>(response, headers, HttpStatus.NOT_FOUND);
			}

			HashMap<String, Response> map = new HashMap<>();
			map.put(BAD_REQUEST, response);
			return new ResponseEntity<>(map, headers, HttpStatus.NOT_FOUND);
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
		
		HttpHeaders headers = new HttpHeaders();

		if ("true".equals(xml))
			headers.setContentType(MediaType.APPLICATION_XML);

		if (passenger == null) {
			Response response = new Response();
			response.setCode("404");
			response.setMsg("Passenger with ID " + id + " does not exist");

			if ("true".equals(xml)) {
				return new ResponseEntity<Response>(response, headers, HttpStatus.NOT_FOUND);
			}

			HashMap<String, Response> map = new HashMap<>();
			map.put(BAD_REQUEST, response);
			return new ResponseEntity<>(map, headers, HttpStatus.NOT_FOUND);
		}
		
		passengerService.deletePassenger(id);
		
		Response response = new Response();
		response.setCode("200");
		response.setMsg("Passenger with ID " + id + " deleted successfully");

		if ("true".equals(xml)) {
			return new ResponseEntity<Response>(response, headers, HttpStatus.OK);
		}

		HashMap<String, Response> map = new HashMap<>();
		map.put(SUCCESS, response);
		return new ResponseEntity<>(map, headers, HttpStatus.OK);
	}
}
