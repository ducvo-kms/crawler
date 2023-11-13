package asia.ducvo.crawler.atheahealth.authentication;

import asia.ducvo.crawler.atheahealth.config.AthenahealthAuthenticationProperties;
import asia.ducvo.crawler.atheahealth.config.AthenahealthProperties;
import asia.ducvo.crawler.config.Pair;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@EnableConfigurationProperties(AthenahealthProperties.class)
public class AthenahealthLoadBalancer {

  private final Map<String, AthenahealthAuthenticationProperties> targets;
  private final Map<String, Semaphore> lockers;

  private final long requestPerDay;

  private final RedisTemplate<String, String> redisTemplate;

  public AthenahealthLoadBalancer(AthenahealthProperties properties, RedisTemplate<String, String> redisTemplate) {
    targets = properties.authentications()
        .parallelStream()
        .collect(Collectors.toMap(i -> i.getCredential().getClientId(), Function.identity()));

    lockers = properties.authentications()
        .parallelStream()
        .map(i -> i.getCredential().getClientId())
        .collect(Collectors.toMap(Function.identity(), i -> new Semaphore(properties.getQuota().getRequestPerSecond())));

    requestPerDay = properties.getQuota().getRequestPerDay();
    this.redisTemplate = redisTemplate;
  }

  public Pair<AthenahealthAuthenticationProperties, Semaphore> getNextCandidate() {
    while(true){
      for (Entry<String, Semaphore> lock : lockers.entrySet()) {
        String count = redisTemplate.opsForValue()
            .getAndExpire(AthenahealthAuthentication.getKeyCountRequest(lock.getKey()), 1, TimeUnit.DAYS);

        if( count != null && Long.parseLong(count) >= requestPerDay){
          log.info("Ignore candidate: {} over limit per day", lock.getKey());
          continue;
        }

        if(lock.getValue().tryAcquire()){
          return new Pair<>(targets.get(lock.getKey()), lock.getValue());
        }
      }
    }
  }
}
