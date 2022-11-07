package edu.sjsu.cmpe275.lab2.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Plane {

	@Column(name = "MODEL")
	private String model;
	
	@Column(name = "CAPACITY")
	private int capacity;
	
	@Column(name = "MANUFACTURER")
	private String manufacturer;
	
	@Column(name = "YEAR_OF_MANUFACTURE")
	private int yearOfManufacture;
	
	public Plane() { }
	
	public Plane( String model, int capacity, String manufacturer, int yearOfManufacture ) {
		
		this.model = model;
		this.capacity = capacity;
		this.manufacturer = manufacturer;
		this.yearOfManufacture = yearOfManufacture;
		
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public int getYearOfManufacture() {
		return yearOfManufacture;
	}

	public void setYearOfManufacture(int yearOfManufacture) {
		this.yearOfManufacture = yearOfManufacture;
	}

	@Override
	public String toString() {
		return "Plane [model=" + model + ", capacity=" + capacity + ", manufacturer=" + manufacturer
				+ ", yearOfManufacture=" + yearOfManufacture + "]";
	}

}
