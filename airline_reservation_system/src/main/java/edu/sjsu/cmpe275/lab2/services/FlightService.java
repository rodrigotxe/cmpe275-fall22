package edu.sjsu.cmpe275.lab2.services;

import java.util.Date;
import java.util.List;

import edu.sjsu.cmpe275.lab2.entities.Flight;

public interface FlightService {

	Flight getFlight(String flightNumber, Date departureDate);
	
	Flight addUpdateFlight(Flight flight);
	
	void deleteFlight(String flightNumber, Date departureDate);
	
	List<Flight> getFlights(String[] flightNumber, Date[] departureDate);
	
	int getIndexOfCapacityFullFlight(List<Flight> flights); // change from boolean to int to get flight index whose capacity is full
	
	int getPrice(List<Flight> flights);
	
	boolean isTimeConflicts(List<Flight> flights);
}
