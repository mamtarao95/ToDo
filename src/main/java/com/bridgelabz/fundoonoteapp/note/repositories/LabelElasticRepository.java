package com.bridgelabz.fundoonoteapp.note.repositories;

import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bridgelabz.fundoonoteapp.note.models.Label;

public interface LabelElasticRepository extends ElasticsearchRepository<Label,String>  {

	void deleteByLabelId(String labelId);

	Optional<Label> findByLabelIdAndUserId(String labelId, String userId);

}
