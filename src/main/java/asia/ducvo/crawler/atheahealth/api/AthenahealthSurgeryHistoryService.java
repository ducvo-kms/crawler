package asia.ducvo.crawler.atheahealth.api;

import asia.ducvo.crawler.atheahealth.config.AthenahealthRestTemplate;
import asia.ducvo.crawler.atheahealth.domain.AthenahealthSurgeryHistory;
import asia.ducvo.crawler.atheahealth.domain.AthenahealthVaccine;
import asia.ducvo.crawler.atheahealth.domain.SurgeryHistorySearchResponse;
import asia.ducvo.crawler.atheahealth.domain.VaccineSearchResponse;
import asia.ducvo.crawler.atheahealth.repository.AthenahealthSurgeryHistoryRepository;
import asia.ducvo.crawler.atheahealth.repository.AthenahealthVaccineRepository;
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
public class AthenahealthSurgeryHistoryService {

  private final AthenahealthRestTemplate restTemplate;

  private final AthenahealthSurgeryHistoryRepository surgeryHistoryRepository;

  @GetMapping("/procedures")
  public void procedures(String practiceId, String patientId, String departmentId) {
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
        .pathSegment("v1", practiceId, "chart", patientId, "surgicalhistory")
        .queryParam("departmentid", departmentId);

    List<AthenahealthSurgeryHistory> surgeryHistories = new ArrayList<>();

    String uri = uriBuilder.build(false).toUriString();
    while (true) {
      ResponseEntity<SurgeryHistorySearchResponse> response = restTemplate
          .getForEntity(uri, SurgeryHistorySearchResponse.class);

      if(!response.getStatusCode().is2xxSuccessful()){
        log.info("Error: {}", response);
      }

      SurgeryHistorySearchResponse body = response.getBody();

      log.info("Body: {}", body);

      if (body != null) {
        surgeryHistories.addAll(body.getProcedures().stream()
            .peek(i -> i.setPracticeId(practiceId))
                .peek(i -> i.setDepartmentId(departmentId))
                .peek(i -> i.setPatientId(patientId))
            .toList());
      }

      if (body == null || body.getNext() == null || body.getNext().isEmpty()) {
        break;
      }

      UriComponents next = UriComponentsBuilder.fromUriString(body.getNext()).build();
      uri = uriBuilder.replaceQueryParam("offset", next.getQueryParams().get("offset")).build(false).toUriString();
    }

    surgeryHistoryRepository.saveAll(surgeryHistories);

  }
}
