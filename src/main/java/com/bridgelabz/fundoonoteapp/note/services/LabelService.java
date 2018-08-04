package com.bridgelabz.fundoonoteapp.note.services;

import java.util.List;

import com.bridgelabz.fundoonoteapp.note.exceptions.LabelNameAlreadyUsedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.LabelNotFoundException;
import com.bridgelabz.fundoonoteapp.note.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonoteapp.note.exceptions.UnAuthorizedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonoteapp.note.models.LabelDTO;
import com.bridgelabz.fundoonoteapp.note.models.NoteDTO;

public interface LabelService {
	
	
	/**
	 * @param userId
	 * @return
	 * @throws LabelNotFoundException
	 */
	List<LabelDTO> getLabels(String userId) throws LabelNotFoundException;

	/**
	 * @param labelName
	 * @param userId
	 * @return
	 * @throws UnAuthorizedException
	 * @throws LabelNotFoundException
	 * @throws LabelNameAlreadyUsedException 
	 */
	LabelDTO createLabel(String labelName, String userId) throws UnAuthorizedException, LabelNotFoundException, LabelNameAlreadyUsedException;

	/**
	 * @param labelName
	 * @param userId
	 * @throws UnAuthorizedException
	 * @throws LabelNotFoundException
	 * @throws UserNotFoundException
	 */
	void deleteLabel(String labelName, String userId) throws UnAuthorizedException, LabelNotFoundException, UserNotFoundException;

	/**
	 * @param labelName
	 * @param userId
	 * @return
	 * @throws UnAuthorizedException
	 * @throws LabelNotFoundException
	 * @throws UserNotFoundException
	 */
	Iterable<NoteDTO> getNotesOfLabel(String labelName, String userId) throws UnAuthorizedException, LabelNotFoundException, UserNotFoundException;

	/**
	 * @param labelName
	 * @param userId
	 * @param noteId
	 * @throws UnAuthorizedException
	 * @throws NoteNotFoundException
	 * @throws LabelNotFoundException
	 * @throws LabelNameAlreadyUsedException
	 * @throws UserNotFoundException 
	 */
	void addLabel(String labelName, String userId, String noteId) throws UnAuthorizedException, NoteNotFoundException, LabelNotFoundException, LabelNameAlreadyUsedException, UserNotFoundException;

	/**
	 * @param labelId
	 * @param userId
	 * @param newLabelName
	 * @throws UnAuthorizedException
	 * @throws LabelNotFoundException
	 * @throws UserNotFoundException
	 */
	void renameLabel(String labelId, String userId, String newLabelName) throws UnAuthorizedException, LabelNotFoundException, UserNotFoundException;

	/**
	 * @param userId
	 * @param labelId
	 * @param noteId
	 * @throws UserNotFoundException
	 * @throws LabelNotFoundException
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 */
	void removeLabel(String userId, String labelId, String noteId) throws UserNotFoundException, LabelNotFoundException, NoteNotFoundException, UnAuthorizedException;

	

}
