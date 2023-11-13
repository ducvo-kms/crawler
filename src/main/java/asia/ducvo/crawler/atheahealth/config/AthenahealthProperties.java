package asia.ducvo.crawler.atheahealth.config;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "athenahealth")
public class AthenahealthProperties {
  private List<AthenahealthCredentialProperties> apps;
  private List<String> scopes;
  private String grantType;
  private String authenticationUrl;
  private String baseUrl;
  private QuotaAthenahealthProperties quota;

  public List<AthenahealthAuthenticationProperties> authentications(){
    return apps.parallelStream()
        .map(i ->AthenahealthAuthenticationProperties.builder()
            .authenticationUrl(authenticationUrl)
            .baseUrl(baseUrl)
            .credential(i)
            .scopes(scopes)
            .grantType(grantType)
            .build())
        .toList();
  }
}
