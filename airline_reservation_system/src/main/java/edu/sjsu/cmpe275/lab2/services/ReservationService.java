package edu.sjsu.cmpe275.lab2.services;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import edu.sjsu.cmpe275.lab2.entities.Flight;
import edu.sjsu.cmpe275.lab2.entities.Reservation;

public interface ReservationService {
	
	Reservation getReservation(String id);
	
	Reservation makeReservation(Reservation reservation);
	
	Reservation updateReservation(Reservation reservation);
	
	void cancelReservation(String id);

	Date[] parse(String departureDates) throws ParseException;
	
	String getReservationConflictNumber(List<Flight> flights, List<Reservation> reservations);
	
//	boolean isSameDepartureDates(Date[] departureDatesForFlightsAdded, Date[] departureDatesForFlightsRemoved);
	
	boolean isFlightsExist(Reservation reservation, List<Flight> flights);
	
	List<Flight> removeFlightsFromReservation(Reservation reservation, List<Flight> flights); 
}
