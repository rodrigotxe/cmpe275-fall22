package edu.sjsu.cmpe275.lab2.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.lab2.entities.Flight;
import edu.sjsu.cmpe275.lab2.entities.FlightKey;
import edu.sjsu.cmpe275.lab2.entities.Reservation;
import edu.sjsu.cmpe275.lab2.repos.FlightRepository;

@Service
public class FlightServiceImpl implements FlightService {

	private static final Logger LOG = LoggerFactory.getLogger(FlightServiceImpl.class);

	private final FlightRepository flightRepository;
	
	@Autowired
	public FlightServiceImpl(FlightRepository flightRepository) {
		this.flightRepository = flightRepository;
	}

	/**
	 * Returns flight entity from database if found. Else, returns null.
	 * @param flightNumber flight number of flight
	 * @param departureDate departure date of flight
	 * @return
	 */
	@Override
	@Transactional
	public Flight getFlight(String flightNumber, Date departureDate) {
		Flight flight = null;

		FlightKey id = new FlightKey(flightNumber, departureDate);

		try {
			flight = flightRepository.findById(id).get();
		} catch (Exception e) {
			return null;
		}

		return flight;
	}

	/**
	 * Updates and returns flight entity from database.
	 * @param newFlight flight entity to be updated
	 * @return
	 */
	@Override
	@Transactional
	public Flight addUpdateFlight(Flight newFlight) {
		return flightRepository.save(newFlight);
	}

	/**
	 * Deletes flight from database.
	 * @param flightNumber flight number of flight
	 * @param departureDate departure date of flight
	 * @return
	 */
	@Override
	@Transactional
	public void deleteFlight(String flightNumber, Date departureDate) {
		FlightKey id = new FlightKey(flightNumber, departureDate);
		flightRepository.deleteById(id);
	}

	/**
	 * Returns list of flight entities. If there are no flights, returns empty list.
	 * @param flightNumbers array of flight numbers of flights
	 * @param departureDates array of departure dates of flights
	 * @return
	 */
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

	
	/**
	 * Returns index of flight whose capacity is full.
	 * @param flights list of flight entities
	 * @return
	 */
	@Override
	public int getIndexOfFlightHavingFullCapacity(List<Flight> flights) {
		for (int i = 0; i < flights.size(); i++) {
			Flight flight = flights.get(i);
			int seatsLeft = flight.getSeatsLeft();

			if (seatsLeft == 0)
				return i;
		}
		return -1;
	}

	/**
	 * Returns sum of prices of flights.
	 * @param list of flight entities
	 */
	@Override
	public int getPrice(List<Flight> flights) {
		int price = 0;

		for (Flight flight : flights) {
			price += flight.getPrice();
		}

		return price;
	}

	/**
	 * Returns true if there are any time conflicts among flights. Else, returns false.
	 * @param flights list of flight entities
	 * @return
	 */
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

				for (int j = i + 1; j < size; j++) {
					Date currDepartureTime = departureTimes[j];

					if (currDepartureTime.after(prevDepartureTime) && currDepartureTime.before(prevArrivalTime)
							|| (currDepartureTime.equals(prevDepartureTime)
									|| currDepartureTime.equals(prevArrivalTime)))
						return true;
				}
			}
		}

		return false;
	}

	/**
	 * Updates seats for flights based on creation or cancellation of reservation.
	 * @param flights list of flight entities
	 * @param reserve true for reservation, false for cancellation
	 * @return
	 */
	@Override
	public void updateSeats(List<Flight> flights, boolean reserve) {
		for (Flight flight : flights) {
			int seatsLeft = flight.getSeatsLeft();
			if (reserve) {
				flight.setSeatsLeft(seatsLeft - 1);
			} else {
				flight.setSeatsLeft(seatsLeft + 1);
			}
			addUpdateFlight(flight);
		}
	}
	
	// update each flight with passenger occupied
//	@Override
//	@Transactional
//	public void addPassengerToFlights(List<Flight> flights, Passenger passenger) {
//		for (Flight flight : flights) {
//			List<Passenger> passengersList = flight.getPassengers();
//			List<Reservation> reservationsList = flight.getReservations();
//			
//			if (passengersList == null) {
//				passengersList = new ArrayList<>();
//			}
//			
//			if (reservationsList == null) {
//				reservationsList = new ArrayList<>();
//			}
//			
//			passengersList.add(passenger);
////			reservationsList.add(reservation);
//			flight.setPassengers(passengersList);
//			// call add or update method to update passenger added to flight
//			 addUpdateFlight(flight);
//			passengerService.updatePassenger(passenger);
////			flight.setReservations(reservationsList);
//		}
//	}

	/**
	 * Returns true if there is any flight conflicts for given flight among existing flights in reservation. Else, returns false.
	 * @param flight flight entity
	 * @return 
	 */
	@Override
	public boolean hasFlightConflict( Flight flight ) {
		
		for( Reservation reservation : flight.getReservations() ) {
			
			for( Flight currentFlight : reservation.getFlights() ) {
				
				if( !currentFlight.getFlightKey().equals(flight.getFlightKey()) ) {
					
//					System.out.println( "Flight 1: " + currentFlight.getDepartureTime().toString() + " " + currentFlight.getArrivalTime().toString() );
//					System.out.println( "Flight 2: " + flight.getDepartureTime().toString() + " " + flight.getArrivalTime().toString() );
//					
					if ( ! ( flight.getDepartureTime().after( currentFlight.getArrivalTime() ) || 
						 flight.getArrivalTime().before( currentFlight.getDepartureTime() ) ) ) {
						
						return true;
						
					} 
						
				}
					
			}
			
		}
		
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