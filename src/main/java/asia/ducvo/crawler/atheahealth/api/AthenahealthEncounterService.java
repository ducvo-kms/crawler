package asia.ducvo.crawler.atheahealth.api;

import asia.ducvo.crawler.atheahealth.config.AthenahealthRestTemplate;
import asia.ducvo.crawler.atheahealth.domain.AthenahealthDocument;
import asia.ducvo.crawler.atheahealth.domain.AthenahealthEncounter;
import asia.ducvo.crawler.atheahealth.domain.DocumentSearchResponse;
import asia.ducvo.crawler.atheahealth.domain.EncounterSearchResponse;
import asia.ducvo.crawler.atheahealth.repository.AthenahealthDocumentRepository;
import asia.ducvo.crawler.atheahealth.repository.AthenahealthEncounterRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("/atheahealth")
@AllArgsConstructor
public class AthenahealthEncounterService {

  private final AthenahealthRestTemplate restTemplate;

  private final AthenahealthEncounterRepository encounterRepository;

  @GetMapping("/encounters")
  public void encounters(String practiceId, String patientId, String departmentId) {
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
        .pathSegment("v1", practiceId, "chart", patientId, "encounters")
        .queryParam("departmentid", departmentId);

    List<AthenahealthEncounter> vaccines = new ArrayList<>();

    String uri = uriBuilder.build(false).toUriString();
    while (true) {
      ResponseEntity<EncounterSearchResponse> response = restTemplate
          .getForEntity(uri, EncounterSearchResponse.class);

      if(!response.getStatusCode().is2xxSuccessful()){
        log.info("Error: {}", response);
      }

      EncounterSearchResponse body = response.getBody();

      log.info("Body: {}", body);

      if (body != null) {
        vaccines.addAll(body.getEncounters().stream()
            .peek(i -> i.setPracticeId(practiceId))
            .toList());
      }

      if (body == null || body.getNext() == null || body.getNext().isEmpty()) {
        break;
      }

      UriComponents next = UriComponentsBuilder.fromUriString(body.getNext()).build();
      uri = uriBuilder.replaceQueryParam("offset", next.getQueryParams().get("offset")).build(false).toUriString();
    }

    encounterRepository.saveAll(vaccines);

  }
}
