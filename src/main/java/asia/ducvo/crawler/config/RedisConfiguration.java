package asia.ducvo.crawler.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@AllArgsConstructor
public class RedisConfiguration {
  @Bean(name = "redisTemplate")
  RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory){
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());

    return template;
  }


  @Bean(name = "redisTemplateLong")
  RedisTemplate<String, Long> redisTemplateLong(RedisConnectionFactory redisConnectionFactory){
    RedisTemplate<String, Long> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericToStringSerializer<>(Long.class) );
    return template;
  }
}
