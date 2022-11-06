package edu.sjsu.cmpe275.lab2.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.sjsu.cmpe275.lab2.entities.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, String> {

	@Query("from Passenger where phone=:phone")
	Passenger findByPhone(@Param("phone") String phone);

}
