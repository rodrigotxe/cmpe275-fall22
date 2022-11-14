package edu.sjsu.cmpe275.lab2.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.lab2.entities.Flight;
import edu.sjsu.cmpe275.lab2.entities.Passenger;
import edu.sjsu.cmpe275.lab2.repos.PassengerRepository;

@Service
public class PassengerServiceImpl implements PassengerService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PassengerServiceImpl.class);

	private final PassengerRepository passengerRepository;

	@Autowired
	public PassengerServiceImpl(PassengerRepository passengerRepository) {

		this.passengerRepository = passengerRepository;

	}

	/**
	 * Returns passenger entity from database if found. Else, returns null.
	 * @param id identifier of passenger
	 * @return
	 */
	@Override
	@Transactional
	public Passenger getPassenger(String id) {
		
		Passenger passenger = null;
		
		try {
			
			passenger = passengerRepository.findById(id).get();
			
		} catch(Exception e) {
			
			return null;
			
		}

		return passenger;
	}

	/**
	 * Returns newly saved passenger into database.
	 * @param newPassenger passenger entity to be saved
	 * @return
	 */
	@Override
	@Transactional
	public Passenger addPassenger(Passenger newPassenger) {

		return passengerRepository.save(newPassenger);
		
	}

	/**
	 * Updates passenger entity into database. Returns updated entity.
	 * @param passenger passenger entity
	 */
	@Override
	@Transactional
	public Passenger updatePassenger(Passenger passenger) {

		checkPassengerID(passenger.getId());

		return passengerRepository.save(passenger);
	}

	/**
	 * Checks if passenger already exists.
	 * @param passengerID identifier of passenger
	 * @return
	 */
	@Transactional
	private void checkPassengerID(String passengerID) {

		if ( !passengerRepository.existsById( passengerID ) )
			
			throw new IllegalStateException("passengerID: Passenger ID " + passengerID + " does not exits");
	}

	/**
	 * Deletes passenger entity from database.
	 * @param id identifier of passenger
	 * @return
	 */
	@Override
	@Transactional
	public void deletePassenger(String id) {
		
		passengerRepository.deleteById(id);
		
	}

	/**
	 * Returns passenger entity with given phone number if found. Else, return null.
	 * @param phone
	 * @return
	 */
	@Override
	@Transactional
	public Passenger findByPhone(String phone) {
		return passengerRepository.findByPhone(phone);
	}
	
	/**
	 * Updates passenger with flights.
	 * @param passenger passenger entity
	 * @param flights list of flight entities
	 * @return
	 */
	@Override
	@Transactional
	public void updatePassengerWithFlights(Passenger passenger, List<Flight> flights) {
		passenger.setFlights(flights);
		updatePassenger(passenger);
	}
}
