package edu.sjsu.cmpe275.lab2.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.cmpe275.lab2.entities.Flight;
import edu.sjsu.cmpe275.lab2.entities.FlightKey;
import edu.sjsu.cmpe275.lab2.entities.Plane;
import edu.sjsu.cmpe275.lab2.services.FlightService;
import edu.sjsu.cmpe275.lab2.util.ResponseUtil;

@RestController
@CrossOrigin(origins = "*")
public class FlightController {
	
	@Autowired
	private FlightService flightService;

	@RequestMapping(value = "/flight/{flightNumber}/{departureDate}")
	public ResponseEntity<?>  getFlight( @PathVariable("flightNumber") String flightNumber,
			                  			 @PathVariable("departureDate") String departureDateS, 
			                  			 @RequestParam("xml") String xml) {
		
		boolean xmlView = "true".equals(xml);

		HttpHeaders headers = new HttpHeaders();
		
		Date departureDate;
		
		try {
			
			departureDate = new SimpleDateFormat("yy-mm-dd" ).parse( departureDateS );
		
		} catch (ParseException e) {
			
			return ResponseUtil.customResponse( "404", e.getMessage(), ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.NOT_FOUND );
			
		}
		
		Flight flight = flightService.getFlight(flightNumber, departureDate );

		if ( xmlView )
			
			headers.setContentType( MediaType.APPLICATION_XML );

		if ( flight == null ) {
			
			return ResponseUtil.customResponse( "404", "Sorry, the requested flight with number " + flightNumber + " does not exist", ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.NOT_FOUND );
			
		}

		return new ResponseEntity<Flight>( flight, headers, HttpStatus.OK );
		
	}
	
	@RequestMapping(value = "/flight/{flightNumber}/{departureDate}", method = RequestMethod.POST)
	public ResponseEntity<?>  createFlight( @PathVariable("flightNumber") String flightNumber,
										    @PathVariable("departureDate") String departureDateS, 
										    @RequestParam("departureTime") String departureTime, 
										    @RequestParam("arrivalTime") String arrivalTime,
										    @RequestParam("price") int price,
										    @RequestParam("origin") String origin, 
										    @RequestParam("destination") String destination,
										    @RequestParam("description") String description, 
										    @RequestParam("capacity") int capacity,
										    @RequestParam("model") String model, 
										    @RequestParam("manufacturer") String manufacturer,
										    @RequestParam("yearOfManufacture") int yearOfManufacture, 
										    @RequestParam("xml") String xml) {
		
		boolean xmlView = "true".equals(xml);

		HttpHeaders headers = new HttpHeaders();
		
		Date departureDate;
		
		try {
			
			departureDate = new SimpleDateFormat("yy-mm-dd" ).parse( departureDateS );
		
		} catch (ParseException e) {
			
			return ResponseUtil.customResponse( "404", e.getMessage(), ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.NOT_FOUND );
			
		}
		
		Flight flight = flightService.getFlight( flightNumber, departureDate );

		if ( xmlView )
			
			headers.setContentType(MediaType.APPLICATION_XML);

		if ( flight == null ) {
			
			flight = new Flight();
			
			flight.setFlightKey( new FlightKey( flightNumber, departureDate ) );
			
		}

		flight.setPrice( price );
		flight.setOrigin( origin );
		flight.setDestination( destination );
		
		try {
			
			flight.setDepartureTime( new SimpleDateFormat("yy-mm-dd-hh" ).parse( departureTime ) );
			flight.setArrivalTime( new SimpleDateFormat("yy-mm-dd-hh" ).parse( arrivalTime ) );
			
		} catch (ParseException e) {
			
			return ResponseUtil.customResponse( "404", e.getMessage(), ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.NOT_FOUND );
			
		}
		
		flight.setDescription( description );
		
		Plane plane = new Plane( model, capacity, manufacturer, yearOfManufacture );
		
		flight.setPlane( plane );

		Flight updatedFlight = flightService.addUpdateFlight( flight );

		return new ResponseEntity<Flight>( updatedFlight, headers, HttpStatus.OK );
		
	}

	@RequestMapping(value = "/airline/{flightNumber}/{departureDate}", method = RequestMethod.DELETE)
	public ResponseEntity<?>  deleteFlight( @PathVariable("flightNumber") String flightNumber,
										    @PathVariable("departureDate") String departureDateS, 
										    @RequestParam("xml") String xml) {
		
		boolean xmlView = "xml".equals(xml);
		
		HttpHeaders headers = new HttpHeaders();
		
		Date departureDate;
		
		try {
			
			departureDate = new SimpleDateFormat("yy-mm-dd" ).parse( departureDateS );
		
		} catch (ParseException e) {
			
			return ResponseUtil.customResponse( "404", e.getMessage(), ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.NOT_FOUND );
			
		}
		
		Flight flight = flightService.getFlight( flightNumber, departureDate );
		
		if (xmlView)
			
			headers.setContentType(MediaType.APPLICATION_XML);

		if ( flight == null ) {
			
			return ResponseUtil.customResponse("404", "Flight with number " + flightNumber + " and departure date " + departureDate + " does not exist", ResponseUtil.BAD_REQUEST, xmlView, headers, HttpStatus.NOT_FOUND);
		
		}

		flightService.deleteFlight( flightNumber, departureDate );

		return ResponseUtil.customResponse("200", "Flight with number " + flightNumber + " deleted successfully", ResponseUtil.SUCCESS, xmlView, headers, HttpStatus.OK );
		
	}
	
}