package com.bridgelabz.fundoonoteapp.user.repositories;

import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository {
   
    public String find(String uuid);

    public void save(String uuid, String userId);
}