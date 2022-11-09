package edu.sjsu.cmpe275.lab2.services;

import edu.sjsu.cmpe275.lab2.entities.Reservation;

public interface ReservationService {
	
	Reservation getReservation(String id);
	
	Reservation makeReservation(Reservation reservation);
	
	Reservation updateReservation(Reservation reservation);
	
	void cancelReservation(String id);

}
