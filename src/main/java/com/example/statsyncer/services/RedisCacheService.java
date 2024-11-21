package com.example.statsyncer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


@Service
public class RedisCacheService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisCacheService(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    // Збереження об'єкта в Redis із вказанням часу зберігання (1 година)
    public void saveToCache(String key, Object value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, Duration.ofHours(1));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // Отримання об'єкта з Redis
    public Object getFromCache(String key, Class<?> clazz) {
        String json = redisTemplate.opsForValue().get(key);
        if (json != null) {
            try {
                return objectMapper.readValue(json, clazz);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // Збереження об'єкта в Redis із можливістю додавання нових елементів до існуючих даних, час зберігання(1 година)
    public <T> void saveListToCache(String key, T value, Class<T> clazz) {
        try {
            List<T> currentData = getListFromCache(key, clazz);

            if (currentData == null) {
                currentData = new ArrayList<>();
            }
            currentData.add(value);

            String json = objectMapper.writeValueAsString(currentData);
            redisTemplate.opsForValue().set(key, json, Duration.ofHours(1));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // Отримання списку об'єктів з Redis
    public <T> List<T> getListFromCache(String key, Class<T> clazz) {
        String json = redisTemplate.opsForValue().get(key);
        if (json != null) {
            try {
                return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // Перевірка наявності ключа в Redis
    public boolean existsInCache(String key) {
        return redisTemplate.hasKey(key);
    }

    // Видалення об'єкта з Redis
    public void deleteFromCache(String key) {
        redisTemplate.delete(key);
    }

    // Видалення всіх записів з Redis
    public void deleteAll() {
        try {
            // Отримуємо всі ключі
            var keys = redisTemplate.keys("*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

