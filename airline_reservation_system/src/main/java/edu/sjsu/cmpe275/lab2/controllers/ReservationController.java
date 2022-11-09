package edu.sjsu.cmpe275.lab2.controllers;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

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

	@RequestMapping(value = "/reservation", method = RequestMethod.POST)
	public ResponseEntity<?> makeReservation(@RequestParam("passengerId") String passengerId,
			@RequestParam("flightNumbers") String flightNumbers, @RequestParam("departureDates") String departureDates,
			@RequestParam("xml") String xml) {

		boolean xmlView = "xml".equals(xml);

		HttpHeaders headers = new HttpHeaders();

		if (xmlView)
			headers.setContentType(MediaType.APPLICATION_XML);

		Date[] dateOfDepartures;

		// 1. parse given dates
		try {
			dateOfDepartures = reservationService.parse(departureDates);
		} catch (ParseException ex) {
			return ResponseUtil.customResponse("400", "Please provide all dates in YYYY-MM-DD format",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		String[] flightIds = flightNumbers.split(",");

		// 2. check for flights available
		List<Flight> flights = flightService.getFlights(flightIds, dateOfDepartures);

		if (flights.isEmpty()) {
			return ResponseUtil.customResponse("400", "There are no flights with given criteria",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		// 3. check for time conflicts among given flights
		boolean isTimeconflict = flightService.isTimeConflicts(flights);

		if (isTimeconflict) {
			return ResponseUtil.customResponse("400",
					"Reservation cannot be made due to time conflicts for selected flights. To proceed for Reservation, Please select different flights which has no time conflicts",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		// 4. check for passenger existence
		Passenger passenger = passengerService.getPassenger(passengerId);

		if (passenger == null) {
			return ResponseUtil.customResponse("400",
					"Reservation cannot be made as passenger with the ID" + passengerId + "does not exist",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		List<Reservation> reservations = passenger.getReservations();

		// 5. check for time conflict with existing reservations
		boolean isTimeConflictWithExistingReservation = reservationService
				.isTimeConflictWithExistingReservations(reservations);

		if (isTimeConflictWithExistingReservation) {
			return ResponseUtil.customResponse("400",
					"Conflict with the existing reservation. Please choose a different available flight",
					ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		// 6. check for capacity
		int index = flightService.getIndexOfFlightHavingFullCapacity(flights);

		if (index != -1) {
			return ResponseUtil.customResponse("400",
					"Flight capacity is full. Please choose a different available flight", ResponseUtil.BAD_REQUEST,
					xmlView, headers, HttpStatus.BAD_REQUEST);
		}

		// if every check is passed, then create a reservation object and save the
		// entity to database
		String origin = flights.get(0).getOrigin();
		String destination = flights.get(flights.size() - 1).getDestination();
		int price = flightService.getPrice(flights);

		Reservation reservation = new Reservation();
		reservation.setOrigin(origin);
		reservation.setDestination(destination);
		reservation.setPassenger(passenger);
		reservation.setFlights(flights);
		reservation.setPrice(price);

		Reservation createdReservation = reservationService.makeReservation(reservation);
		
		flightService.updateSeats(flights);

		return new ResponseEntity<Reservation>(createdReservation, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/reservation/{number}", method = RequestMethod.PUT)
	public Reservation updateReservation(@PathVariable("number") String reservationNumber,
			@RequestParam("flightsAdded") String flightsAdded, @RequestParam("flightsRemoved") String flightsRemoved,
			@RequestParam("xml") String xml) {
		// TODO
		return null;
	}

	@RequestMapping( value = "/reservation/{number}", method = RequestMethod.DELETE )
	public ResponseEntity<?>  cancelReservation( @PathVariable("number") String reservationNumber,
										         @RequestParam("xml") String xml ) {
		
		boolean xmlView = xml.equals("true");
		
		HttpHeaders headers = new HttpHeaders();
		
		if ( xmlView )
			
			headers.setContentType( MediaType.APPLICATION_XML );
		
		Reservation reservation = reservationService.getReservation( reservationNumber );
		
		if ( reservation == null ) {
			
			return ResponseUtil.customResponse("404", "Reservation with number " + reservationNumber + " does not exist", ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.NOT_FOUND );
		}
		
		reservationService.cancelReservation(reservationNumber );
		
		return ResponseUtil.customResponse("200", "Reservation with number " + reservationNumber + " is canceled successfully ", ResponseUtil.SUCCESS, xmlView, headers, HttpStatus.OK );
		
		
	}
}
