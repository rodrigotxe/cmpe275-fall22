package edu.sjsu.cmpe275.lab2.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sjsu.cmpe275.lab2.entities.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, String> {

}
