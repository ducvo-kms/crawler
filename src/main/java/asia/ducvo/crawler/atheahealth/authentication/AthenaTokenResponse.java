package asia.ducvo.crawler.atheahealth.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AthenaTokenResponse {

  @JsonProperty("token_type")
  private String tokenType;
  @JsonProperty("expires_in")
  private int expiresIn;
  @JsonProperty("access_token")
  private String accessToken;
  private String scope;
}
