package edu.sjsu.cmpe275.lab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@CrossOrigin(origins = "*")
public class PassengerController {

	@Autowired
	private PassengerService passengerService;

	@RequestMapping(value = "/passenger/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getPassenger(@PathVariable("id") String id, @RequestParam("xml") String xml) {
		Passenger passenger = passengerService.getPassenger(id);
		return ResponseEntity.ok().contentType("true".equals(xml) ? MediaType.APPLICATION_XML : MediaType.APPLICATION_JSON).body(passenger);
	}

	@RequestMapping(value = "/passenger", method = RequestMethod.POST, produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> createPassenger(@RequestParam("firstname") String firstName,
			@RequestParam("lastname") String lastName, @RequestParam("birthyear") int birthYear,
			@RequestParam("gender") String gender, @RequestParam("phone") String phone,
			@RequestParam("xml") String xml) {

		Passenger newPassenger = new Passenger();

		newPassenger.setFirstName( firstName );
		newPassenger.setLastName( lastName );
		newPassenger.setBirthYear( birthYear );
		newPassenger.setGender( gender );
		newPassenger.setPhone( phone );

		Passenger createdPassenger = passengerService.addPassenger( newPassenger );

		return ResponseEntity.ok().contentType("true".equals(xml) ? MediaType.APPLICATION_XML : MediaType.APPLICATION_JSON).body(createdPassenger);
	}

	@RequestMapping(value = "/passenger/{id}", method = RequestMethod.PUT, produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> updatePassenger(@PathVariable("id") String id, @RequestParam("firstname") String firstName,
			@RequestParam("lastname") String lastName, @RequestParam("birthyear") int birthYear,
			@RequestParam("gender") String gender, @RequestParam("phone") String phone,
			@RequestParam("xml") String xml) {

		Passenger passenger = new Passenger();

		passenger.setFirstName( firstName );
		passenger.setLastName( lastName );
		passenger.setBirthYear( birthYear );
		passenger.setGender( gender );
		passenger.setPhone( phone );

		Passenger updatedPassenger = passengerService.updatePassenger( passenger );

		return ResponseEntity.ok().contentType("true".equals(xml) ? MediaType.APPLICATION_XML : MediaType.APPLICATION_JSON).body(updatedPassenger);
	}

	@RequestMapping(value = "/passenger/{id}", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> deletePassenger(@PathVariable("id") String id, @RequestParam("xml") String xml) {
		passengerService.deletePassenger(id);
		return null;
	}
}
