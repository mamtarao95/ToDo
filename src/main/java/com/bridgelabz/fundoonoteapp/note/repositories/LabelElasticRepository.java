package com.bridgelabz.fundoonoteapp.note.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bridgelabz.fundoonoteapp.note.models.Label;

public interface LabelElasticRepository extends ElasticsearchRepository<Label,String>  {

	void deleteByLabelId(String labelId);

}
