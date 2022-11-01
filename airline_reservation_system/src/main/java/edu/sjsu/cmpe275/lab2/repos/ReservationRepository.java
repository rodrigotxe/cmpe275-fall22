package edu.sjsu.cmpe275.lab2.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sjsu.cmpe275.lab2.entities.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

}
