package com.bridgelabz.fundoonoteapp.note.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonoteapp.note.exceptions.LabelNameAlreadyUsedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.LabelNotFoundException;
import com.bridgelabz.fundoonoteapp.note.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonoteapp.note.exceptions.UnAuthorizedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonoteapp.note.models.LabelDTO;
import com.bridgelabz.fundoonoteapp.note.models.NoteDTO;
import com.bridgelabz.fundoonoteapp.note.services.NoteService;
import com.bridgelabz.fundoonoteapp.user.models.Response;

@RestController
@RequestMapping("/label") 
public class LabelController {

	@Autowired
	private NoteService noteService;

	/**
	 * @param request
	 * @param userId
	 * @return
	 * @throws LabelNotFoundException
	 */
	@GetMapping("/getall")
	public Iterable<LabelDTO> getLabels(HttpServletRequest request, @RequestHeader("userId") String userId)
			throws LabelNotFoundException {
		
		return noteService.getLabels(userId);
	}

	/**
	 * @param labelName
	 * @param userId
	 * @param request
	 * @return
	 * @throws UnAuthorizedException
	 * @throws LabelNotFoundException
	 * @throws LabelNameAlreadyUsedException 
	 */
	@PostMapping("/create")
	public ResponseEntity<LabelDTO> createLabel(@RequestParam String labelName, @RequestHeader("userId") String userId,
			HttpServletRequest request) throws UnAuthorizedException, LabelNotFoundException, LabelNameAlreadyUsedException {
		
		LabelDTO labelViewDTO = noteService.createLabel(labelName, userId);

		return new ResponseEntity<>(labelViewDTO, HttpStatus.CREATED);
	}

	/**
	 * @param labelId
	 * @param userId
	 * @param request
	 * @return
	 * @throws UnAuthorizedException
	 * @throws LabelNotFoundException
	 * @throws UserNotFoundException
	 */
	@DeleteMapping("/delete")
	public ResponseEntity<Response> deleteLabel(@RequestParam String labelId, @RequestHeader("userId") String userId,
			HttpServletRequest request) throws UnAuthorizedException, LabelNotFoundException, UserNotFoundException {
		
		noteService.deleteLabel(labelId, userId);
		
		Response responseDTO = new Response();
		responseDTO.setMessage("Label deleted Successfully!!");
		responseDTO.setStatus(10);
		
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);

	}

	/**
	 * @param labelId
	 * @param noteId
	 * @param userId
	 * @param request
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws LabelNotFoundException
	 * @throws LabelNameAlreadyUsedException
	 * @throws UserNotFoundException
	 */
	@PutMapping("/add")
	public ResponseEntity<Response> addLabel(@RequestParam String labelId, @RequestParam("noteId") String noteId, // pathvar
			@RequestHeader("userId") String userId, HttpServletRequest request) throws NoteNotFoundException,
			UnAuthorizedException, LabelNotFoundException, LabelNameAlreadyUsedException, UserNotFoundException {

		noteService.addLabel(labelId, userId, noteId);

		Response responseDTO = new Response();
		responseDTO.setMessage("Label added Successfully!!");
		responseDTO.setStatus(11);
		
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);

	}

	/**
	 * @param labelId
	 * @param newLabelName
	 * @param userId
	 * @param request
	 * @return
	 * @throws UnAuthorizedException
	 * @throws LabelNotFoundException
	 * @throws UserNotFoundException
	 */
	@PutMapping("/rename")
	public ResponseEntity<Response> renameLabel(@RequestParam String labelId, @RequestParam String newLabelName,
			@RequestHeader("userId") String userId)
			throws UnAuthorizedException, LabelNotFoundException, UserNotFoundException {
		
		noteService.renameLabel(labelId, userId, newLabelName);
		
		Response responseDTO = new Response();
		responseDTO.setMessage("Label renamed Successfully!!");
		responseDTO.setStatus(12);
		
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	/**
	 * @param labelName
	 * @param userId
	 * @param request
	 * @return
	 * @throws UnAuthorizedException
	 * @throws LabelNotFoundException
	 * @throws UserNotFoundException
	 */
	@GetMapping("/getnotesoflabel")
	public Iterable<NoteDTO> getNotesOfLabel(@RequestBody String labelName, @RequestHeader("userId") String userId,
			HttpServletRequest request) throws UnAuthorizedException, LabelNotFoundException, UserNotFoundException {
		
		return noteService.getNotesOfLabel(labelName, userId);

	}

	

	/**
	 * @param labelId
	 * @param noteId
	 * @param userId
	 * @return
	 * @throws UserNotFoundException
	 * @throws LabelNotFoundException
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 */
	@DeleteMapping("/removelabel")
	public ResponseEntity<Response> removeLabel(@RequestParam String labelId, @RequestParam String noteId,
			@RequestHeader("userId") String userId)
			throws UserNotFoundException, LabelNotFoundException, NoteNotFoundException, UnAuthorizedException {
		
		noteService.removeLabel(userId, labelId, noteId);
		
		Response responseDTO = new Response();
		responseDTO.setMessage("Label removed Successfully!!");
		responseDTO.setStatus(13);
		
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);

	}

}
