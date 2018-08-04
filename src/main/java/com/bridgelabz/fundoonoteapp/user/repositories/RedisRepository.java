package com.bridgelabz.fundoonoteapp.user.repositories;

import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository {
   
    public String getValue(String uuid);

    public void save(String uuid, String userId);

    public Long delete(String uuid);
	
}