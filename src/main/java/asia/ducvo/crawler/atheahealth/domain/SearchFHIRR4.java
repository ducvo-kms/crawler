package asia.ducvo.crawler.atheahealth.domain;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SearchFHIRR4 <T>{
  private List<Link> link;

  private List<T> entry;
}
