package asia.ducvo.crawler.atheahealth.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class VaccineInformationStatement {

  @JsonProperty("clinicalorderclassid")
  private int clinicalOrderClassId;

}
