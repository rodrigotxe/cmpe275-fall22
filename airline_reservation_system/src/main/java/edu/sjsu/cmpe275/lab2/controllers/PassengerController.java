package edu.sjsu.cmpe275.lab2.controllers;

import edu.sjsu.cmpe275.lab2.services.PassengerService;
import edu.sjsu.cmpe275.lab2.services.PassengerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.cmpe275.lab2.entities.Passenger;

@RestController
@CrossOrigin(origins = "*")
public class PassengerController {

	@Autowired
	private PassengerServiceImpl passengerService;

	@RequestMapping(value = "/passenger/{id}")
	public Passenger getPassenger(@PathVariable("id") String id, @RequestParam("xml") String xml) {
		// TODO
		return null;
	}

	@RequestMapping(value = "/passenger", method = RequestMethod.POST)
	public Passenger createPassenger(@RequestParam("firstname") String firstName,
			@RequestParam("lastname") String lastName, @RequestParam("birthyear") int birthYear,
			@RequestParam("gender") String gender, @RequestParam("phone") String phone,
			@RequestParam("xml") String xml) {

		Passenger newPassenger = new Passenger();

		newPassenger.setFirstName( firstName );
		newPassenger.setLastName( lastName );
		newPassenger.setBirthYear( birthYear );
		newPassenger.setGender( gender );
		newPassenger.setPhone( phone );

		passengerService.addPassenger( newPassenger );

		return null;
	}

	@RequestMapping(value = "/passenger/{id}", method = RequestMethod.PUT)
	public Passenger updatePassenger(@PathVariable("id") String id, @RequestParam("firstname") String firstName,
			@RequestParam("lastname") String lastName, @RequestParam("birthyear") int birthYear,
			@RequestParam("gender") String gender, @RequestParam("phone") String phone,
			@RequestParam("xml") String xml) {

		Passenger passenger = passengerService.getPassenger( id );

		passenger.setFirstName( firstName );
		passenger.setLastName( lastName );
		passenger.setBirthYear( birthYear );
		passenger.setGender( gender );
		passenger.setPhone( phone );

		passengerService.updatePassenger( passenger );

		return null;
	}

	@RequestMapping(value = "/passenger/{id}", method = RequestMethod.DELETE)
	public Passenger deletePassenger(@PathVariable("id") String id, @RequestParam("xml") String xml) {
		// TODO
		return null;
	}
}
