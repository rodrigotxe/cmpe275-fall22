package edu.sjsu.cmpe275.lab2.services;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import edu.sjsu.cmpe275.lab2.entities.Reservation;

public interface ReservationService {
	
	Reservation getReservation(String id);
	
	Reservation makeReservation(Reservation reservation);
	
	Reservation updateReservation(Reservation reservation);
	
	void cancelReservation(String id);

	Date[] parse(String departureDates) throws ParseException;
	
	boolean isTimeConflictWithExistingReservations(List<Reservation> reservations);
}
