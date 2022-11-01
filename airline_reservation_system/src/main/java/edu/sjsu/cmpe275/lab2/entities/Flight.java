package edu.sjsu.cmpe275.lab2.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FLIGHT")
public class Flight {

	@EmbeddedId
	private FlightKey flightKey;

	@Column(name = "DEPARTURE_TIME")
	private Date departureTime;

	@Column(name = "ARRIVAL_TIME")
	private Date arrivalTime;

	@Column(name = "PRICE")
	private int price;

	@Column(name = "ORIGIN")
	private String origin;

	@Column(name = "DESTINATION")
	private String destination;

	@Column(name = "SEATS_LEFT")
	private int seatsLeft;

	@Column(name = "DESCRIPTION")
	private String description;

	@Embedded
	private Plane plane;

	private List<Passenger> passengers;

	public FlightKey getFlightKey() {
		return flightKey;
	}

	public void setFlightKey(FlightKey flightKey) {
		this.flightKey = flightKey;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
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

	public int getSeatsLeft() {
		return seatsLeft;
	}

	public void setSeatsLeft(int seatsLeft) {
		this.seatsLeft = seatsLeft;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Plane getPlane() {
		return plane;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}

	public List<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}

	@Override
	public String toString() {
		return "Flight [flightKey=" + flightKey + ", departureTime=" + departureTime + ", arrivalTime=" + arrivalTime
				+ ", price=" + price + ", origin=" + origin + ", destination=" + destination + ", seatsLeft="
				+ seatsLeft + ", description=" + description + ", plane=" + plane + ", passengers=" + passengers + "]";
	}
	
	@Embeddable
	public class FlightKey {

		@Column(name = "FLIGHT_NUMBER", nullable = false)
		private String flightNumber;

		@Column(name = "DEPARTURE_DATE", nullable = false)
		private Date departureDate;

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

	}


}
