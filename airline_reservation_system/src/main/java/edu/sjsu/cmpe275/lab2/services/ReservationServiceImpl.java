package edu.sjsu.cmpe275.lab2.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.lab2.entities.Flight;
import edu.sjsu.cmpe275.lab2.entities.Reservation;
import edu.sjsu.cmpe275.lab2.repos.ReservationRepository;

@Service
public class ReservationServiceImpl implements ReservationService {

	private static final Logger LOG = LoggerFactory.getLogger(ReservationServiceImpl.class);

	private final ReservationRepository reservationRepository;

	@Autowired
	public ReservationServiceImpl(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	/**
	 * Returns reservation entity from database if found. Else, returns null.
	 * @param id identifier of reservation
	 * @return
	 */
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

	/**
	 * Returns newly saved reservation into database.
	 * @param reservation reservation entity to be saved
	 * @return
	 */
	@Override
	@Transactional
	public Reservation makeReservation(Reservation reservation) {
		return reservationRepository.save(reservation);
	}

	/**
	 * Updates reservation entity into database. Returns updated entity.
	 * @param reservation reservation entity
	 */
	@Override
	@Transactional
	public Reservation updateReservation(Reservation reservation) {
		return reservationRepository.save(reservation);
	}

	/**
	 * Deletes reservation entity from database.
	 * @param id identifier of reservation
	 * @return
	 */
	@Override
	@Transactional
	public void cancelReservation(String id) {
		reservationRepository.deleteById(id);
	}

	/**
	 * Parses the dates given in comma separated values in String. Returns array of Data objects.
	 * @param departureDates departure dates of flights
	 * @return
	 */
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

	/**
	 * Checks for any conflicts with existing reservations. Returns conflicted reservation number if conflicts exists. Else, returns null.
	 * @param flights list of flight entities
	 * @param reservations list of resevation entities
	 * @return
	 */
	@Override
	public String getReservationConflictNumber(List<Flight> flights, List<Reservation> reservations) {

		for (Reservation reservation : reservations) {
			List<Flight> reservedFlights = reservation.getFlights();

			for (Flight reservedFlight : reservedFlights) {
				Date reservedFlightDepartureTime = reservedFlight.getDepartureTime();
				Date reservedFlightArrivalTime = reservedFlight.getArrivalTime();

				for (int i = 0; i < flights.size(); i++) {
					Flight toBeReservedFlight = flights.get(i);
					Date toBeReservedFlightDepartureTime = toBeReservedFlight.getDepartureTime();

					if (toBeReservedFlightDepartureTime.after(reservedFlightDepartureTime)
							&& toBeReservedFlightDepartureTime.before(reservedFlightArrivalTime)
							|| (toBeReservedFlightDepartureTime.equals(reservedFlightDepartureTime)
									|| toBeReservedFlightDepartureTime.equals(reservedFlightArrivalTime))) {
						return reservation.getReservationNumber();
					}
				}
			}
		}

		return null;
	}

//	@Override
//	public boolean isSameDepartureDates(Date[] departureDatesForFlightsAdded, Date[] departureDatesForFlightsRemoved) {
//
//		int m = departureDatesForFlightsAdded.length, n = departureDatesForFlightsRemoved.length;
//
//		return departureDatesForFlightsAdded[0].equals(departureDatesForFlightsRemoved[0])
//				&& departureDatesForFlightsAdded[m - 1].equals(departureDatesForFlightsRemoved[n - 1]);
//	}
	
	/**
	 * Returns true if flights exists in given reservation. Else, returns false.
	 * @param reservation reservation entity
	 * @param flights list of flight entities
	 * @return
	 */
	@Override
	public boolean isFlightsExist(Reservation reservation, List<Flight> flights) {
		
		List<Flight> reservedFlights = reservation.getFlights();
		
		for (Flight reservedFlight : reservedFlights) {
			for (Flight flight : flights) {
				if (flight.equals(reservedFlight)) return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns list of flight entities with given flights removed from given reservation.
	 * @param reservation reservation entity
	 * @param flights lift of flight entities
	 * @return
	 */
	@Override
	public List<Flight> removeFlightsFromReservation(Reservation reservation, List<Flight> flights) {
		List<Flight> reservedFlights = reservation.getFlights();
		
		for (int i = 0; i < flights.size(); i++) {
			reservedFlights.remove(flights.get(i));
		}
		
		return reservedFlights;
	}
}
