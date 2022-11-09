package edu.sjsu.cmpe275.lab2.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.lab2.entities.Flight;
import edu.sjsu.cmpe275.lab2.entities.Reservation;
import edu.sjsu.cmpe275.lab2.repos.ReservationRepository;

@Service
public class ReservationServiceImpl implements ReservationService {

	private final ReservationRepository reservationRepository;

	@Autowired
	public ReservationServiceImpl(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Override
	@Transactional
	public Reservation getReservation(String id) {

		Reservation reservation = null;

		try {
			reservation = reservationRepository.findById(id).get();
		} catch (Exception e) {
			reservation = null;
		}

		return reservation;
	}

	@Override
	@Transactional
	public Reservation makeReservation(Reservation reservation) {
		return reservationRepository.save(reservation);
	}

	@Override
	@Transactional
	public Reservation updateReservation(Reservation reservation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void cancelReservation(String id) {
		// TODO Auto-generated method stub

	}
	
	// parses the dates given in comma separated values in String
	@Override
	public Date[] parse(String departureDates) throws ParseException {
		String[] dates = departureDates.split(",");
		Date[] dateOfDepartures = new Date[dates.length];
		
		for (int i = 0; i < dates.length; i++) {
			String date = dates[i];
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				dateOfDepartures[i] = sdf.parse(date);
			} catch (ParseException ex) {
				throw ex;
			}
		}
		
		return dateOfDepartures;
	}
	
	// checks for any conflicts with existing reservations
	@Override
	public boolean isTimeConflictWithExistingReservations(List<Flight> flights, List<Reservation> reservations) {
		
		for (Reservation reservation : reservations) {
			List<Flight> reservedFlights = reservation.getFlights();
			
			for (Flight reservedFlight : reservedFlights) {
				Date reservedFlightDepartureTime = reservedFlight.getDepartureTime();
				Date reservedFlightArrivalTime = reservedFlight.getArrivalTime();
				
				for (int i = 0; i < flights.size(); i++) {
					Flight toBeReservedFlight = flights.get(i);
					Date toBeReservedFlightDepartureTime = toBeReservedFlight.getDepartureTime();
					
					if (toBeReservedFlightDepartureTime.after(reservedFlightDepartureTime) && toBeReservedFlightDepartureTime.before(reservedFlightArrivalTime)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
