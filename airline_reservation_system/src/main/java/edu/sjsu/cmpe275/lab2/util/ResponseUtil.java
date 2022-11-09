package edu.sjsu.cmpe275.lab2.util;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.sjsu.cmpe275.lab2.response.Response;

public class ResponseUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(ResponseUtil.class);

	public static final String BAD_REQUEST = "BadRequest";
	public static final String SUCCESS = "Success";

	public static ResponseEntity<?> customResponse(String code, String message, String tag, boolean xmlView,
			HttpHeaders headers, HttpStatus status) {
		LOG.info("Executing customResponse() << ");
		
		Response response = new Response();

		response.setCode(code);
		response.setMsg(message);

		if (xmlView)
			return new ResponseEntity<Response>(response, headers, status);

		HashMap<String, Response> map = new HashMap<>();
		map.put(tag, response);

		LOG.info("Exiting customResponse() >>");
		
		return new ResponseEntity<>(map, headers, status);

	}
}
