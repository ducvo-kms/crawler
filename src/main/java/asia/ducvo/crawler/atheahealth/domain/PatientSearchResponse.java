package asia.ducvo.crawler.atheahealth.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PatientSearchResponse {
  @JsonProperty("totalcount")
  int totalCount;

  String next;

  List<AthenahealthPatient> patients;
}
