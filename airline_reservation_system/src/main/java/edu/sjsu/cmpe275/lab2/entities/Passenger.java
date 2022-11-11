package edu.sjsu.cmpe275.lab2.entities;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "PASSENGER")
public class Passenger {

	@Id
	@Column(name = "PASSENGER_ID")
	private String id;
	
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	@Column(name = "LAST_NAME")
	private String lastName;
	
	@Column(name = "BIRTH_YEAR")
	private int birthYear;
	
	@Column(name = "GENDER")
	private String gender;
	
	@Column(name = "PHONE")
	private String phone;
	
	@JsonIgnoreProperties({"price", "passenger", "flights"})
	@JsonManagedReference
	@OneToMany(mappedBy = "passenger")
	private List<Reservation> reservations;

	@JsonIgnoreProperties({"price", "description", "plane", "passengers", "reservations"})
	@ManyToMany
	@JoinTable( name = "passenger_flights",
			joinColumns = @JoinColumn(name = "passenger_id"),
			inverseJoinColumns = { @JoinColumn(name = "flight_number"), @JoinColumn(name = "departure_date") } )
	private List<Flight> flights;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}

	@Override
	public String toString() {
		return "Passenger [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", birthYear="
				+ birthYear + ", gender=" + gender + ", phone=" + phone + "]";
	}

}
