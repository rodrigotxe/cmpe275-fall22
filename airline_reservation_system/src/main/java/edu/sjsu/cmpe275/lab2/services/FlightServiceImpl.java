package edu.sjsu.cmpe275.lab2.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	
	@Override
	@Transactional
	public List<Flight> getFlights(String[] flightNumbers, Date[] departureDates) {
		List<Flight> flights = new ArrayList<>();
		
		for (int i = 0; i < flightNumbers.length; i++) {
			String flightNumber = flightNumbers[i];
			Date departureDate = departureDates[i];
			
			Flight flight = getFlight(flightNumber, departureDate);
			
			if (flight != null) {
				flights.add(flight);
			}
		}
		
		return flights;
	}
	
	@Override
	public int getIndexOfFlightHavingFullCapacity(List<Flight> flights) {
		
		for (int i = 0; i < flights.size(); i++) {
			Flight flight = flights.get(i);
			int seatsLeft = flight.getSeatsLeft();
			
			if (seatsLeft == 0) return i;
		}
		return -1;
	}
	
	@Override
	public int getPrice(List<Flight> flights) {
		int price = 0;
		
		for (Flight flight : flights) {
			price += flight.getPrice();
		}
		
		return price;
	}
	
	@Override
	public boolean isTimeConflicts(List<Flight> flights) {
		// TODO
		return false;
	}
	
//	@Override
//	@Transactional
//	public boolean checkForValidFlights(List<Flight> flights) {
//		for (int i = 0; i < flights.size(); i++) {
//			Flight flight = flights.get(i);
//			
//			if (flight != null) return true;
//		}
//		
//		return false;
//	}

}