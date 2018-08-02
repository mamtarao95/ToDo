package com.bridgelabz.fundoonoteapp.note.exceptionhandler;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.fundoonoteapp.note.exceptions.LabelNameAlreadyUsedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.LabelNotFoundException;
import com.bridgelabz.fundoonoteapp.note.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonoteapp.note.exceptions.NoteNotTrashedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.ReminderDateNotValidException;
import com.bridgelabz.fundoonoteapp.note.exceptions.UnAuthorizedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonoteapp.user.globalexceptionhandler.GlobalExceptionHandler;
import com.bridgelabz.fundoonoteapp.user.models.Response;

@ControllerAdvice
public class NoteExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> genericExceptionhandler(HttpServletRequest request, Exception exception) {
		logger.info("Generic Exception Occured: URL=" + request.getRequestURL(), exception);
		Response response = new Response();
		response.setMessage(exception.getMessage());
		response.setStatus(-1);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoteNotFoundException.class)
	public ResponseEntity<Response> noteException(NoteNotFoundException exception,HttpServletRequest request) {
		logger.info("Note Exception Occured: URL=" + request.getRequestURL());
		Response response = new Response();
		response.setMessage(exception.getMessage());
		response.setStatus(-2);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}
	
	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<Response> unauthorizedException(UnAuthorizedException exception,HttpServletRequest request) {
		logger.info("UnAuthorized Exception Occured: URL=" + request.getRequestURL());
		Response response = new Response();
		response.setMessage(exception.getMessage());
		response.setStatus(-3);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}
	
	@ExceptionHandler(LabelNotFoundException.class)
	public ResponseEntity<Response> labelException(LabelNotFoundException exception,HttpServletRequest request) {
		logger.info("LabelNotFoundException Occured: URL=" + request.getRequestURL());
		Response response = new Response();
		response.setMessage(exception.getMessage());
		response.setStatus(-2);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}
	
	@ExceptionHandler(NoteNotTrashedException.class)
	public ResponseEntity<Response> noteNotTrashedException(NoteNotTrashedException exception,HttpServletRequest request) {
		logger.info("NoteNotTrashedException Occured: URL=" + request.getRequestURL());
		Response response = new Response();
		response.setMessage(exception.getMessage());
		response.setStatus(-2);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}
	
	@ExceptionHandler(ReminderDateNotValidException.class)
	public ResponseEntity<Response> reminderDateNotValidException(ReminderDateNotValidException exception,HttpServletRequest request) {
		logger.info("ReminderDateNotValidException  Occured: URL=" + request.getRequestURL());
		Response response = new Response();
		response.setMessage(exception.getMessage());
		response.setStatus(-2);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Response> userNotFoundException(UserNotFoundException exception,HttpServletRequest request) {
		logger.info("UserNotFoundException Occured: URL=" + request.getRequestURL());
		Response response = new Response();
		response.setMessage(exception.getMessage());
		response.setStatus(-2);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}
	@ExceptionHandler(LabelNameAlreadyUsedException.class)
	public ResponseEntity<Response> labelNameAlreadyUsedException(LabelNameAlreadyUsedException exception,HttpServletRequest request) {
		logger.info("LabelNameAlreadyUsedException Occured: URL=" + request.getRequestURL());
		Response response = new Response();
		response.setMessage(exception.getMessage());
		response.setStatus(-2);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

	
	

}
