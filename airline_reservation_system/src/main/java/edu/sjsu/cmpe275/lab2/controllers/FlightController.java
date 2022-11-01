package edu.sjsu.cmpe275.lab2.controllers;

import java.util.Date;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.cmpe275.lab2.entities.Flight;

@RestController
@CrossOrigin(origins = "*")
public class FlightController {

	@RequestMapping(value = "/flight/{flightNumber}/{departureDate}")
	public Flight getFlight(@PathVariable("flightNumber") String flightNumber,
			@PathVariable("departureDate") Date departureDate, @RequestParam("xml") String xml) {
		// TODO
		return null;
	}

	@RequestMapping(value = "/flight/{flightNumber}/{departureDate}", method = RequestMethod.POST)
	public Flight updateFlight(@PathVariable("flightNumber") String flightNumber,
			@PathVariable("departureDate") Date departureDate, @RequestParam("price") int price,
			@RequestParam("origin") String origin, @RequestParam("destination") String destination,
			@RequestParam("departureTime") String departureTime, @RequestParam("arrivalTime") String arrivalTime,
			@RequestParam("description") String description, @RequestParam("capacity") int capacity,
			@RequestParam("model") String model, @RequestParam("manufacturer") String manufacturer,
			@RequestParam("yearOfManufacture") int yearOfManufacture, @RequestParam("xml") String xml) {
		// TODO
		return null;
	}

	@RequestMapping(value = "/airline/{flightNumber}/{departureDate}", method = RequestMethod.DELETE)
	public Flight deleteFlight(@PathVariable("flightNumber") String flightNumber,
			@PathVariable("departureDate") Date departureDate, @RequestParam("xml") String xml) {
		// TODO
		return null;
	}
}
