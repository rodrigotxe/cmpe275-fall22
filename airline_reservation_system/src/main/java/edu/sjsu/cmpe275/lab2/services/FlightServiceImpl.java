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
	
	// returns index of flight whose capacity is full
	@Override
	public int getIndexOfFlightHavingFullCapacity(List<Flight> flights) {
		
		for (int i = 0; i < flights.size(); i++) {
			Flight flight = flights.get(i);
			int seatsLeft = flight.getSeatsLeft();
			
			if (seatsLeft == 0) return i;
		}
		return -1;
	}
	
	// returns cumulative price of flights
	@Override
	public int getPrice(List<Flight> flights) {
		int price = 0;
		
		for (Flight flight : flights) {
			price += flight.getPrice();
		}
		
		return price;
	}
	
	// checks for any time conflicts between flights
	@Override
	public boolean isTimeConflicts(List<Flight> flights) {
		int size = flights.size();
		Date[] departureTimes = new Date[size];
		Date[] arrivalTimes = new Date[size];
		
		for (int i = 0; i < size; i++) {
			Flight flight = flights.get(i);
			
			departureTimes[i] = flight.getDepartureTime();
			arrivalTimes[i] = flight.getArrivalTime();
		}
		
		if (size > 1) {
			for (int i = 0; i < size; i++) {
				Date prevDepartureTime = departureTimes[i];
				Date prevArrivalTime = arrivalTimes[i];
				
				for (int j = i+1; j < size; j++) {
					Date currDepartureTime = departureTimes[j];
					
					if (currDepartureTime.after(prevDepartureTime) && currDepartureTime.before(prevArrivalTime)) return true;
				}
			}
		}
		
		return false;
	}
	
	// update seats for flights based on creation or cancellation of reservation
	@Override
	public void updateSeats(List<Flight> flights, boolean reserve) {
		for (Flight flight : flights) {
			int seatsLeft = flight.getSeatsLeft();
			if (reserve) {
				flight.setSeatsLeft(seatsLeft-1);
			} else {
				flight.setSeatsLeft(seatsLeft+1);
			}
			addUpdateFlight(flight);
		}
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