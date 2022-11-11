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

	@Override
	@Transactional
	public Passenger addPassenger(Passenger newPassenger) {

		return passengerRepository.save(newPassenger);
		
	}

	@Override
	@Transactional
	public Passenger updatePassenger(Passenger passenger) {

		checkPassengerID(passenger.getId());

		return passengerRepository.save(passenger);
	}

	@Transactional
	private void checkPassengerID(String passengerID) {

		if ( !passengerRepository.existsById( passengerID ) )
			
			throw new IllegalStateException("passengerID: Passenger ID " + passengerID + " does not exits");
	}

	@Override
	@Transactional
	public void deletePassenger(String id) {
		
		passengerRepository.deleteById(id);
		
	}

	@Override
	@Transactional
	public Passenger findByPhone(String phone) {
		return passengerRepository.findByPhone(phone);
	}
	
	@Override
	@Transactional
	public void updatePassengerWithFlights(Passenger passenger, List<Flight> flights) {
		passenger.setFlights(flights);
		updatePassenger(passenger);
	}
}
