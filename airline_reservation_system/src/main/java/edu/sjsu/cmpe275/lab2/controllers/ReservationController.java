package edu.sjsu.cmpe275.lab2.controllers;

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

import edu.sjsu.cmpe275.lab2.entities.Reservation;
import edu.sjsu.cmpe275.lab2.services.ReservationService;
import edu.sjsu.cmpe275.lab2.util.ResponseUtil;

@RestController
@CrossOrigin(origins = "*")
public class ReservationController {
	
	@Autowired
	private ReservationService reservationService;

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

	@RequestMapping(value = "/reservation", method = RequestMethod.POST)
	public Reservation makeReservation(@RequestParam("passengerId") String id,
			@RequestParam("flightNumbers") String flightNumbers, @RequestParam("xml") String xml) {
		// TODO
		return null;
	}

	@RequestMapping(value = "/reservation/{number}", method = RequestMethod.PUT)
	public Reservation updateReservation(@PathVariable("number") String reservationNumber,
			@RequestParam("flightsAdded") String flightsAdded, @RequestParam("flightsRemoved") String flightsRemoved,
			@RequestParam("xml") String xml) {
		// TODO
		return null;
	}

	@RequestMapping(value = "/reservation/{number}", method = RequestMethod.DELETE)
	public Reservation cancelReservation(@PathVariable("number") String reservationNumber,
			@RequestParam("xml") String xml) {
		// TODO
		return null;
	}
}
