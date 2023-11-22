package asia.ducvo.crawler.atheahealth.config;

import java.net.URI;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@AllArgsConstructor
@EnableConfigurationProperties(AthenahealthProperties.class)
public class AthenahealthRestTemplate {

  private final RestTemplate restTemplate;
  private final AthenahealthProperties properties;

  public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType)
      throws RestClientException {

    log.info("Athenahealth request: {}", url);

    UriComponents urlComponent = UriComponentsBuilder.fromUriString(url)
        .build();

    UriComponentsBuilder athenahealth = UriComponentsBuilder
        .fromHttpUrl(properties.getBaseUrl());


    if(urlComponent.getPath() != null){
      athenahealth.path(urlComponent.getPath());
    }

    athenahealth.queryParams(urlComponent.getQueryParams());

    return restTemplate.getForEntity(athenahealth.build(false).toUriString(), responseType);
  }

  public <T> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity,
      ParameterizedTypeReference<T> responseType) throws RestClientException {


    log.info("Athenahealth request: {}", url);

    UriComponents urlComponent = UriComponentsBuilder.fromUriString(url)
        .build();


    UriComponentsBuilder athenahealth = UriComponentsBuilder
        .fromHttpUrl(properties.getBaseUrl());


    if(urlComponent.getPath() != null){
      athenahealth.path(urlComponent.getPath());
    }

    athenahealth.queryParams(urlComponent.getQueryParams());

    return restTemplate.exchange(athenahealth.build(false).toUriString(), method, requestEntity, responseType);
  }

}
