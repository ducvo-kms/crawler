package asia.ducvo.crawler.atheahealth.config;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AthenahealthAuthenticationProperties {
  private String baseUrl;
  private String authenticationUrl;
  private AthenahealthCredentialProperties credential;
  private List<String> scopes;
  private String grantType;

  public String getId(){
    return  credential.getClientId();
  }
}
