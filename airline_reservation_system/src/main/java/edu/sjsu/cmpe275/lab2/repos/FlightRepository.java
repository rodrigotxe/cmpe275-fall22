package edu.sjsu.cmpe275.lab2.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sjsu.cmpe275.lab2.entities.Flight;
import edu.sjsu.cmpe275.lab2.entities.FlightKey;

public interface FlightRepository extends JpaRepository<Flight, FlightKey> {

}