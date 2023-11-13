package asia.ducvo.crawler.atheahealth.config;

import lombok.Data;

@Data
public class QuotaAthenahealthProperties {
  private int requestPerSecond;
  private int requestPerDay;
}
