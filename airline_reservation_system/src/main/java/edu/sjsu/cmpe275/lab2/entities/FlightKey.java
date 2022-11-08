package edu.sjsu.cmpe275.lab2.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FlightKey implements Serializable {

	private static final long serialVersionUID = -275818048782291291L;
	
	@Column(name = "FLIGHT_NUMBER", nullable = false)
	private String flightNumber;

	@Column(name = "DEPARTURE_DATE", nullable = false)
	private Date departureDate;

	public FlightKey( ) { }

	public FlightKey( String flightNumber, Date departureDate ) {

		this.flightNumber = flightNumber;
		this.departureDate = departureDate;

	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}
	
	@Override
    public boolean equals(Object o) {
		
        if (this == o) return true;
        
        if (o == null || getClass() != o.getClass() ) return false;
        
        FlightKey flightKey = (FlightKey) o;
        
        return this.flightNumber.equals(flightKey.getFlightNumber() ) &&
               this.departureDate.equals(flightKey.getDepartureDate() );
    }

    @Override
    public int hashCode() {
    	
        return Objects.hash( this.flightNumber, this.departureDate );
        
    }

}