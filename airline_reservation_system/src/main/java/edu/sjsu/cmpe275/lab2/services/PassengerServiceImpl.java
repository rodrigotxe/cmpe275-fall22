package edu.sjsu.cmpe275.lab2.services;

import edu.sjsu.cmpe275.lab2.entities.Passenger;
import edu.sjsu.cmpe275.lab2.repos.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PassengerServiceImpl implements PassengerService {

	private final PassengerRepository passengerRepository;

	@Autowired
	public PassengerServiceImpl(PassengerRepository passengerRepository) {

		this.passengerRepository = passengerRepository;

	}

	@Override
	@Transactional
	public Passenger getPassenger(String id) {

		return passengerRepository.findById(id).get();
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

	private void checkPassengerID(String passengerID) {

		if (!passengerRepository.existsById(passengerID))
			throw new IllegalStateException("passengerID: Passenger ID " + passengerID + " does not exits");
	}

	@Override
	public void deletePassenger(String id) {
		passengerRepository.deleteById(id);
	}

}
