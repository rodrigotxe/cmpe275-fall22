package edu.sjsu.cmpe275.lab2.services;

import edu.sjsu.cmpe275.lab2.entities.Passenger;
import edu.sjsu.cmpe275.lab2.repos.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassengerServiceImpl implements PassengerService {
    
    private final PassengerRepository passengerRepository;

    @Autowired
    public PassengerServiceImpl( PassengerRepository passengerRepository ) {

        this.passengerRepository = passengerRepository;

    }

    public Passenger getPassenger( String id ) {

        return new Passenger();

    }

    public void addPassenger( Passenger newPassenger ) {

        passengerRepository.save( newPassenger );

    }

    public void updatePassenger( Passenger passenger ) {

        checkPassengerID( passenger.getId() );

        passengerRepository.save( passenger );

    }

    private void checkPassengerID( String passengerID ) {

        if( ! passengerRepository.existsById( passengerID ) )

            throw new IllegalStateException("passengerID: Passenger ID " + passengerID + " does not exits");

    }

}
