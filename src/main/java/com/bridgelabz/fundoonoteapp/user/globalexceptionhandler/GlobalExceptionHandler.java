package com.bridgelabz.fundoonoteapp.user.globalexceptionhandler;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.bridgelabz.fundoonoteapp.user.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonoteapp.user.exceptions.LoginException;
import com.bridgelabz.fundoonoteapp.user.exceptions.IncorrectPasswordException;
import com.bridgelabz.fundoonoteapp.user.exceptions.UserNotActivatedException;
import com.bridgelabz.fundoonoteapp.user.models.Response;

@ControllerAdvice
public class GlobalExceptionHandler {


	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	 /*@ExceptionHandler(Exception.class) public ResponseEntity<Response>
	  genericExceptionhandler(HttpServletRequest request, Exception exception) {
	  logger.info("Generic Exception Occured: URL=" + request.getRequestURL());
	  Response response = new Response();
	  response.setMessage(exception.getMessage()); response.setStatus(0);
	  return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); 
	  }*/
	 

	@ExceptionHandler(IncorrectPasswordException.class)
	public ResponseEntity<Response> registrationExceptionHandler(IncorrectPasswordException exception,
			HttpServletRequest request) {
		logger.info("Exception encountered at " + request.getRequestURI() + exception.getMessage());
		Response response = new Response();
		response.setMessage(exception.getMessage());
		response.setStatus(-1);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(LoginException.class)
	public ResponseEntity<Response> loginExceptionHandler(LoginException exception, HttpServletRequest request) {
		logger.info("Exception encountered at " + request.getRequestURI() + ":  " + exception.getMessage());
		Response response = new Response();
		response.setMessage(exception.getMessage());
		response.setStatus(-2);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserNotActivatedException.class)
	public ResponseEntity<Response> userActivationExceptionHandler(UserNotActivatedException exception,
			HttpServletRequest request) {
		logger.info("Exception encountered at " + request.getRequestURI() + ":  " + exception.getMessage());
		Response response = new Response();
		response.setMessage(exception.getMessage());
		response.setStatus(-3);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Response> forgotPasswordExceptionHandler(UserNotFoundException exception,
			HttpServletRequest request) {
		logger.info("Exception occured at " + request.getRequestURI() + ": " + exception.getMessage());
		Response response = new Response();
		response.setMessage(exception.getMessage());
		response.setStatus(-4);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
}