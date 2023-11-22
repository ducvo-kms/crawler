package asia.ducvo.crawler.atheahealth.api;

import asia.ducvo.crawler.atheahealth.config.AthenahealthRestTemplate;
import asia.ducvo.crawler.atheahealth.domain.AthenahealthVaccine;
import asia.ducvo.crawler.atheahealth.domain.VaccineSearchResponse;
import asia.ducvo.crawler.atheahealth.repository.AthenahealthVaccineRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("/atheahealth")
@AllArgsConstructor
public class AthenahealthMedicationRequestService {

  private final AthenahealthRestTemplate restTemplate;

  @GetMapping("/medication-requests")
  public void vaccines(String practiceId, String patientId, String departmentId) {
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
        .pathSegment("v1", practiceId, "chart", patientId, "vaccinesx")
        .queryParam("departmentid", departmentId)
        .queryParam("showdeleted", "true")
        .queryParam("showdeclinedorders", "true")
        .queryParam("showprescribednotadministered", "true")
        .queryParam("showrefused", "true");

    List<AthenahealthVaccine> vaccines = new ArrayList<>();

    String uri = uriBuilder.build(false).toUriString();
    while (true) {
      try {

        ResponseEntity<VaccineSearchResponse> response = restTemplate
            .getForEntity(uri, VaccineSearchResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
          log.info("Error: {}", response);
        }

        VaccineSearchResponse body = response.getBody();

        log.info("Body: {}", body);

        if (body != null) {
          vaccines.addAll(body.getVaccines().stream()
              .peek(i -> i.setPracticeId(practiceId))
              .peek(i -> i.setDepartmentId(departmentId))
              .peek(i -> i.setPatientId(patientId))
              .toList());
        }

        if (body == null || body.getNext() == null || body.getNext().isEmpty()) {
          break;
        }

        UriComponents next = UriComponentsBuilder.fromUriString(body.getNext()).build();
        uri = uriBuilder.replaceQueryParam("offset", next.getQueryParams().get("offset"))
            .build(false).toUriString();
      }catch (RestClientException exception){
        log.info("Error", exception);
      }
    }


  }
}
