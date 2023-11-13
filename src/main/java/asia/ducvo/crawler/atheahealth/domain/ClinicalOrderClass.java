package asia.ducvo.crawler.atheahealth.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
class ClinicalOrderClass {

  @JsonProperty("name")
  private String name;

  @JsonProperty("clinicalorderclassid")
  private int clinicalOrderClassId;
}
