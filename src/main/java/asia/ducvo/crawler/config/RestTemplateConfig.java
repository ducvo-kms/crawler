package asia.ducvo.crawler.config;

import asia.ducvo.crawler.Authentication;
import asia.ducvo.crawler.atheahealth.config.AthenahealthProperties;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(AthenahealthProperties.class)
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder,
      ObjectProvider<List<Authentication<?>>> authentications) {
    RestTemplate restTemplate = builder
        .setConnectTimeout(Duration.of(15, ChronoUnit.MINUTES))
        .setReadTimeout(Duration.of(15, ChronoUnit.MINUTES))
        .build();



    if (authentications.getIfAvailable() != null) {
      restTemplate.setInterceptors(
          authentications.getIfAvailable().stream().map(Authentication::interceptor).toList()
      );
    }

    return restTemplate;
  }

}
