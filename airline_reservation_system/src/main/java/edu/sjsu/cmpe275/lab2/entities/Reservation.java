package edu.sjsu.cmpe275.lab2.entities;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "RESERVATION")
public class Reservation {

	@Id
	@Column(name = "RESERVATION_NUMBER")
	private String reservationNumber;
	
	@Column(name = "ORIGIN")
	private String origin;
	
	@Column(name = "DESTINATION")
	private String destination;
	
	@Column(name = "PRICE")
	private int price;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PASSENGER_ID")
	private Passenger passenger;

	@ManyToMany
	@JoinTable( name = "reservation_flights",
			joinColumns = @JoinColumn(name = "passenger_number"),
			inverseJoinColumns = { @JoinColumn(name = "flight_number"), @JoinColumn(name = "departure_date") } )
	private List<Flight> flights;

	public String getReservationNumber() {
		return reservationNumber;
	}

	public void setReservationNumber(String reservationNumber) {
		this.reservationNumber = reservationNumber;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Passenger getPassenger() {
		return passenger;
	}

	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}

	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}

	@Override
	public String toString() {
		return "Reservation [reservationNumber=" + reservationNumber + ", origin=" + origin + ", destination="
				+ destination + ", price=" + price + ", passenger=" + passenger + "]";
	}

}
