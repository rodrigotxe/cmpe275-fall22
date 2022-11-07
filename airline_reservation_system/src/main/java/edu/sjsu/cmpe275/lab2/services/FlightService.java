package edu.sjsu.cmpe275.lab2.services;

import java.util.Date;

import edu.sjsu.cmpe275.lab2.entities.Flight;

public interface FlightService {

	Flight getFlight(String flightNumber, Date departureDate);
	
	Flight addUpdateFlight(Flight flight);
	
	void deleteFlight(String flightNumber, Date departureDate);
	
}
