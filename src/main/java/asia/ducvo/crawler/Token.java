package asia.ducvo.crawler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {
  String id;
  String token;
  int ttl;
}
