package com.bridgelabz.fundoonoteapp.note.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.bridgelabz.fundoonoteapp.note.exceptions.LabelNameAlreadyUsedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.LabelNotFoundException;
import com.bridgelabz.fundoonoteapp.note.exceptions.MalformedUrlException;
import com.bridgelabz.fundoonoteapp.note.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonoteapp.note.exceptions.NoteNotTrashedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.ReminderDateNotValidException;
import com.bridgelabz.fundoonoteapp.note.exceptions.UnAuthorizedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonoteapp.note.models.CreateNoteDTO;
import com.bridgelabz.fundoonoteapp.note.models.LabelDTO;
import com.bridgelabz.fundoonoteapp.note.models.Note;
import com.bridgelabz.fundoonoteapp.note.models.UpdateNoteDTO;
import com.bridgelabz.fundoonoteapp.note.models.NoteDTO;

public interface NoteService {

	/**
	 * @param createNoteDTO
	 * @param userId
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws LabelNotFoundException
	 * @throws ReminderDateNotValidException
	 * @throws UserNotFoundException 
	 * @throws IOException 
	 * @throws MalformedUrlException 
	 */
	NoteDTO createNote(CreateNoteDTO createNoteDTO, String userId)
			throws NoteNotFoundException, UnAuthorizedException, LabelNotFoundException, ReminderDateNotValidException, UserNotFoundException, IOException, MalformedUrlException;

	/**
	 * @param token
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 */
	void emptyTrash(String token) throws NoteNotFoundException, UnAuthorizedException;

	/**
	 * @param updateNoteDTO
	 * @param token
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws UserNotFoundException 
	 */
	void updateNote(UpdateNoteDTO updateNoteDTO, String token) throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException;

	/**
	 * @param noteId
	 * @param token
	 * @param input
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws NoteNotTrashedException
	 * @throws UserNotFoundException 
	 */
	void deleteOrRestoreTrashedNote(String noteId, String token, boolean input)
			throws NoteNotFoundException, UnAuthorizedException, NoteNotTrashedException, UserNotFoundException;

	/**
	 * @param token
	 * @param noteId
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws UserNotFoundException 
	 */
	void trashNote(String token, String noteId) throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException;

	/**
	 * @param noteId
	 * @param token
	 * @param reminder
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws ReminderDateNotValidException
	 * @throws UserNotFoundException 
	 */
	void addReminder(String noteId, String token, Date reminder) throws NoteNotFoundException, UnAuthorizedException, ReminderDateNotValidException, UserNotFoundException;

	/**
	 * @param noteId
	 * @param token
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws UserNotFoundException 
	 */
	void removeReminder(String noteId, String token) throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException;

	/**
	 * @param noteId
	 * @param token
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws UserNotFoundException 
	 */
	NoteDTO viewNote(String noteId, String token) throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException;

	/**
	 * @param token
	 * @return
	 * @throws NoteNotFoundException
	 */
	List<NoteDTO> viewAllNotes(String token) throws NoteNotFoundException;

	/**
	 * @param userId
	 * @return
	 * @throws NoteNotFoundException
	 * @throws NoteNotTrashedException
	 */
	Iterable<NoteDTO> viewAllTrashedNotes(String userId) throws NoteNotFoundException, NoteNotTrashedException;

	/**
	 * @param userId
	 * @return
	 * @throws NoteNotFoundException
	 */
	Iterable<NoteDTO> getArchiveNotes(String userId) throws NoteNotFoundException;

	/**
	 * @param userId
	 * @param noteId
	 * @throws NoteNotFoundException
	 * @throws UnAuthorizedException
	 * @throws UserNotFoundException 
	 */
	void setArchive(String userId, String noteId) throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException;

	/**
	 * @param userId
	 * @param noteId
	 * @throws UnAuthorizedException
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException 
	 */
	void unArchive(String userId, String noteId) throws UnAuthorizedException, NoteNotFoundException, UserNotFoundException;



	/**
	 * @param noteId
	 * @param userId
	 * @param color
	 * @throws UserNotFoundException 
	 * @throws NoteNotFoundException 
	 * @throws UnAuthorizedException 
	 */
	void changeColour(String noteId, String userId, String color) throws UnAuthorizedException, NoteNotFoundException, UserNotFoundException;

	/**
	 * @param userId
	 * @param noteId
	 * @param isPin
	 * @throws UserNotFoundException 
	 * @throws NoteNotFoundException 
	 * @throws UnAuthorizedException 
	 */
	void pinOrUnpinNote(String userId, String noteId, boolean isPin) throws UnAuthorizedException, NoteNotFoundException, UserNotFoundException;
	public Optional<Note> validateNoteAndUser(String noteId, String userId)
			throws UnAuthorizedException, NoteNotFoundException, UserNotFoundException ;
}
