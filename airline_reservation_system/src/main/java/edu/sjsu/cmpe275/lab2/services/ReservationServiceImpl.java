package edu.sjsu.cmpe275.lab2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		// TODO Auto-generated method stub
		return null;
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
}
