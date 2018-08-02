package com.bridgelabz.fundoonoteapp.user.repositories;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonoteapp.user.models.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	
	public Optional<User> findByEmail(String email);

	public void save(Optional<User> user);

}