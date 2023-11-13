package asia.ducvo.crawler.atheahealth.authentication;

import asia.ducvo.crawler.atheahealth.config.AthenahealthAuthenticationProperties;
import asia.ducvo.crawler.atheahealth.config.AthenahealthProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(AthenahealthProperties.class)
public class AthenahealthInitializer implements ApplicationListener<ApplicationReadyEvent> {

  private final AthenahealthProperties properties;
  private final AthenahealthAuthentication authentication;


  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    for (AthenahealthAuthenticationProperties app : properties.authentications()) {
      authentication.getToken(app);
    }
  }
}
