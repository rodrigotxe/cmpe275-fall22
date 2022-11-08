package edu.sjsu.cmpe275.lab2.util;

import java.util.HashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.sjsu.cmpe275.lab2.response.Response;

public class ResponseUtil {

	public static final String BAD_REQUEST = "BadRequest";
	public static final String SUCCESS = "Success";

	public static ResponseEntity<?> customResponse(String code, String message, String tag, boolean xmlView,
			HttpHeaders headers, HttpStatus status) {

		Response response = new Response(code, message);

		if (xmlView)
			return new ResponseEntity<Response>(response, headers, status);

		HashMap<String, Response> map = new HashMap<>();
		map.put(tag, response);

		return new ResponseEntity<>(map, headers, status);

	}
}
