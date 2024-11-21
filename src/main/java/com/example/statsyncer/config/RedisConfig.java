package com.example.statsyncer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * Конфігурація для налаштування Redis у Spring додатку.
 * Створює RedisTemplate для взаємодії з Redis сервером.
 */
@Configuration
public class RedisConfig {

    /**
     * Налаштовує RedisTemplate для роботи з Redis, використовуючи JSON-серіалізацію.
     * @param connectionFactory Фабрика з'єднання з Redis.
     * @return Налаштований об'єкт RedisTemplate.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Використання Jackson для серіалізації/десеріалізації значень у Redis.
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        template.setDefaultSerializer(serializer);

        // Серіалізація ключів і хеш-ключів у вигляді рядків.
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Серіалізація значень і хеш-значень у форматі JSON.
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        return template;
    }
}
