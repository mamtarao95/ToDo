package com.bridgelabz.fundoonoteapp.user.repositories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisRepositoryImpl implements RedisRepository {
    
	@Value("${redis.key}")
	private String key;
      
    private RedisTemplate<String, String> redisTemplate;
  
    private HashOperations<String, String, String> hashOperations;
    
    @Autowired
    public RedisRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        hashOperations = this.redisTemplate.opsForHash();
    }
  
    @Override
    public void save(String uuid, String userId) {
        hashOperations.put(key, uuid, userId);
    }
 
    @Override
    public String getValue(String uuid) {
        return hashOperations.get(key, uuid);
    }
    
    @Override
    public Long delete(String uuid) {
    	return hashOperations.delete(key, uuid);
    	
    }

}