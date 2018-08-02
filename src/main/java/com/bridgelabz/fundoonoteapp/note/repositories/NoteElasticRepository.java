package com.bridgelabz.fundoonoteapp.note.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.bridgelabz.fundoonoteapp.note.models.Note;

public interface NoteElasticRepository extends ElasticsearchRepository<Note,String>{

	Optional<Note> findById(String noteId);

	void deleteByTrashedAndUserId(boolean b, String userId);

	List<Note> findAllByUserIdAndTrashed(String userId, boolean b);

	List<Note> findAllByUserIdAndArchive(String userId, boolean b);

	//@Query(value = "{ 'userId' : ?0, 'labels.labelId' : ?1 }")

	//@Query(value = " {'bool' :{ 'must' : [{'match':{'userId':?0}}, {'match':{'labels.labelId':?1}}  ]} } ")
	//List<Note> findAllByQuery(String userId, String labelId);
}
