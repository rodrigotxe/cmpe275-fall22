package edu.sjsu.cmpe275.lab2.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.cmpe275.lab2.entities.Reservation;

@RestController
@CrossOrigin(origins = "*")
public class ReservationController {

	@RequestMapping(value = "/reservation/{number}")
	public Reservation getReservation(@PathVariable("number") String reservationNumber,
			@RequestParam("xml") String xml) {
		// TODO
		return null;
	}

	@RequestMapping(value = "/reservation", method = RequestMethod.POST)
	public Reservation makeReservation(@RequestParam("passengerId") String id,
			@RequestParam("flightNumbers") String flightNumbers, @RequestParam("xml") String xml) {
		// TODO
		return null;
	}

	@RequestMapping(value = "/reservation/{number}", method = RequestMethod.POST)
	public Reservation updateReservation(@PathVariable("number") String reservationNumber,
			@RequestParam("flightsAdded") String flightsAdded, @RequestParam("flightsRemoved") String flightsRemoved,
			@RequestParam("xml") String xml) {
		// TODO
		return null;
	}

	@RequestMapping(value = "/reservation/{number}", method = RequestMethod.POST)
	public Reservation cancelReservation(@PathVariable("number") String reservationNumber,
			@RequestParam("xml") String xml) {
		// TODO
		return null;
	}
}
