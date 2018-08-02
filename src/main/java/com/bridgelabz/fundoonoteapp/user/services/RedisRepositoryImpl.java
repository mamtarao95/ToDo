package com.bridgelabz.fundoonoteapp.user.services;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import com.bridgelabz.fundoonoteapp.user.repositories.RedisRepository;

@Repository
public class RedisRepositoryImpl implements RedisRepository {
    private static final String KEY = "TOKEN";
      
    private RedisTemplate<String, String> redisTemplate;
  
    private HashOperations<String, String, String> hashOperations;
  
    @Autowired
    public RedisRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
  
    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }
 
    @Override
    public void save(String uid, String userId) {
        hashOperations.put(KEY, uid, userId);
    }
 
    @Override
    public String find(String uid) {
        return hashOperations.get(KEY, uid);
    }

}