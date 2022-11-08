package edu.sjsu.cmpe275.lab2.services;



import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.lab2.entities.Flight;
import edu.sjsu.cmpe275.lab2.entities.FlightKey;
import edu.sjsu.cmpe275.lab2.repos.FlightRepository;

@Service
public class FlightServiceImpl implements FlightService {
	
	private final FlightRepository flightRepository;

	@Autowired
	public FlightServiceImpl(FlightRepository flightRepository) {

		this.flightRepository = flightRepository;

	}

	@Override
	@Transactional
	public Flight getFlight(String flightNumber, Date departureDate) {
		
		Flight flight = null;
		
		FlightKey id = new FlightKey(flightNumber, departureDate);
		
		try {
			
			flight = flightRepository.findById( id ).get();
			
		} catch(Exception e) {
			
			return null;
			
		}

		return flight;
	}

	@Override
	@Transactional
	public Flight addUpdateFlight(Flight newFlight) {

		return flightRepository.save( newFlight );
		
	}

	@Override
	@Transactional
	public void deleteFlight(String flightNumber, Date departureDate) {
		
		FlightKey id = new FlightKey(flightNumber, departureDate);
		
		flightRepository.deleteById(id);
		
	}

}