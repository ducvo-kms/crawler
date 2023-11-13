package asia.ducvo.crawler.atheahealth.authentication;

import asia.ducvo.crawler.Authentication;
import asia.ducvo.crawler.Token;
import asia.ducvo.crawler.atheahealth.config.AthenahealthAuthenticationProperties;
import asia.ducvo.crawler.config.Pair;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class AthenahealthAuthentication implements
    Authentication<AthenahealthAuthenticationProperties> {

  public static final double FACTOR = 0.8;
  private final RestTemplate restTemplate;
  private final RedisTemplate<String, String> redisTemplate;
  private final AthenahealthLoadBalancer loadBalancer;

  public AthenahealthAuthentication(RestTemplateBuilder restTemplateBuilder,
      RedisTemplate<String, String> redisTemplate, AthenahealthLoadBalancer loadBalancer) {
    restTemplate = restTemplateBuilder.build();
    this.redisTemplate = redisTemplate;
    this.loadBalancer = loadBalancer;
  }

  @Override
  public String getToken(AthenahealthAuthenticationProperties properties) {

    log.info("Renew token for athenahealth: {}", properties.getCredential().getClientId());
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("scope", String.join(" ", properties.getScopes()));
    body.add("grant_type", properties.getGrantType());
    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth(properties.getCredential().getClientId(),
        properties.getCredential().getClientSecret());

    URI uri = UriComponentsBuilder.fromHttpUrl(properties.getAuthenticationUrl()).build().toUri();
    RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, headers,
        HttpMethod.POST, uri);

    ResponseEntity<AthenaTokenResponse> response = restTemplate
        .exchange(request, AthenaTokenResponse.class);

    AthenaTokenResponse responseToken = Objects.requireNonNull(response.getBody());
    Token token = Token.builder()
        .id(properties.getId())
        .token(responseToken.getTokenType() + " " + responseToken.getAccessToken())
        .ttl(responseToken.getExpiresIn())
        .build();

    redisTemplate.opsForValue().set(
        token.getId(),
        token.getToken(),
        Duration.of(Math.round(FACTOR * token.getTtl()), ChronoUnit.SECONDS));

    log.info("Renew token for athenahealth successful: {}",
        properties.getCredential().getClientId());

    return token.getToken();
  }


  public ClientHttpRequestInterceptor interceptor() {
    return (HttpRequest request, byte[] body, ClientHttpRequestExecution execution) -> {
      Pair<AthenahealthAuthenticationProperties, Semaphore> candidate = loadBalancer.getNextCandidate();
      AthenahealthAuthenticationProperties properties = candidate.getKey();
      log.info("Athenahealth use: {}", properties.getId());
      if (isIgnore(request, properties)) {
        candidate.getValue().release();
        return execution.execute(request, body);
      }

      if (isAthenahealth(request, properties)) {
        handleInjectAuthorization(request, properties);
      }

      redisTemplate.opsForValue().increment(
          getKeyCountRequest(properties.getId()),
          1);

      try{
        return execution.execute(request, body);
      }catch (Exception exception){
        log.info("Call athenahealth exception: ", exception);
        throw exception;
      }finally {
        candidate.getValue().release();
      }
    };
  }

  public static String getKeyCountRequest(String id) {
    return "count:" + id +":" + LocalDate.now();
  }

  private synchronized void handleInjectAuthorization(HttpRequest request,
      AthenahealthAuthenticationProperties properties) {

    String token = redisTemplate.opsForValue().get(properties.getId());
    if (token == null) {
        token = getToken(properties);
    }

    request.getHeaders().set(HttpHeaders.AUTHORIZATION, token);
  }

  private boolean isAthenahealth(HttpRequest request,
      AthenahealthAuthenticationProperties properties) {
    return request.getURI().toString().contains(properties.getBaseUrl());
  }

  private boolean isIgnore(HttpRequest request, AthenahealthAuthenticationProperties properties) {
    return request.getURI().toString().contains(properties.getAuthenticationUrl());
  }
}
