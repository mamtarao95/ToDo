package com.bridgelabz.fundoonoteapp.note.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.bridgelabz.fundoonoteapp.note.models.Note;


public interface NoteRespository extends MongoRepository<Note, String>{
	
	Optional<Note> findById(String noteId);

	Optional<Note> findByUserIdAndId(String userId, String noteId);

	Long deleteNoteByTrashed(boolean trashed);

	Optional<Note> save(Optional<Note> note);
	
	void deleteByTrashedAndUserId(boolean b, String id);

	Optional<Note> findOneByUserId(String userId);

	List<Note> findAllByUserIdAndArchive(String userId, boolean b);

	@Query(value = "{ 'userId' : ?0, 'labels.labelId' : ?1 }")
    List<Note> findAllByQuery(String userId,String labelId);

	
}
