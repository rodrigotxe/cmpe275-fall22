package edu.sjsu.cmpe275.lab2.services;

import edu.sjsu.cmpe275.lab2.entities.Passenger;

public interface PassengerService {

	Passenger getPassenger(String id);
	
	Passenger addPassenger(Passenger passenger);
	
	Passenger updatePassenger(Passenger passenger);
	
	void deletePassenger(String id);
	
	Passenger findByPhone(String phone);
}
