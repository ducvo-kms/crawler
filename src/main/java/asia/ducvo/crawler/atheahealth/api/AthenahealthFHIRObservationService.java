package asia.ducvo.crawler.atheahealth.api;

import asia.ducvo.crawler.atheahealth.config.AthenahealthRestTemplate;
import asia.ducvo.crawler.atheahealth.domain.AthenahealthDocument;
import asia.ducvo.crawler.atheahealth.domain.DocumentSearchResponse;
import asia.ducvo.crawler.atheahealth.domain.Link;
import asia.ducvo.crawler.atheahealth.domain.Observation;
import asia.ducvo.crawler.atheahealth.domain.SearchFHIRR4;
import asia.ducvo.crawler.atheahealth.repository.AthenahealthDocumentRepository;
import asia.ducvo.crawler.atheahealth.repository.AthenahealthObservationRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
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
public class AthenahealthFHIRObservationService {

  private final AthenahealthRestTemplate restTemplate;

  private final AthenahealthObservationRepository observationRepository;

  @GetMapping("/observations")
  public void observations(String practiceId, String patientId, String departmentId) {
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
        .pathSegment("fhir", "r4", "Observation")
        .queryParam("patient", String.format("a-%s.E-%s", practiceId, patientId))
        .queryParam("ah-practice", String.format("a-1.Practice-%s", practiceId))
        .queryParam("_count", "1000");

    String uri = uriBuilder.build(false).toUriString();
    while (true) {
      ResponseEntity<SearchFHIRR4<Observation>> response = restTemplate
          .exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
          });

      if(!response.getStatusCode().is2xxSuccessful()){
        log.info("Error: {}", response);
      }

      SearchFHIRR4<Observation> body = response.getBody();

      log.info("Body: {}", body);

      if (body != null) {
        observationRepository.saveAll(body.getEntry().stream()
            .peek(i -> i.setPracticeId(practiceId))
                .peek(i -> i.setDepartmentId(departmentId))
                .peek(i -> i.setPatientId(patientId))
            .toList());
      }

      if (body == null || body.getLink() == null || body.getLink().stream().noneMatch(i -> i.getRelation().equalsIgnoreCase("next"))) {
        break;
      }

      Optional<Link> link = body.getLink().stream()
          .filter(i -> i.getRelation()
              .equalsIgnoreCase("next")).findFirst();

      if(link.isEmpty()){
        break;
      }

      UriComponents next = UriComponentsBuilder.fromUriString(link.get().getUrl()).build();
      uri = uriBuilder.replaceQueryParam("cursor", next.getQueryParams().get("cursor")).build(false).toUriString();
    }

  }
}
