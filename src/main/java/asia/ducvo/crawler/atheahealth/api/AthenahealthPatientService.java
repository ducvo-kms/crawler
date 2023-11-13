package asia.ducvo.crawler.atheahealth.api;

import asia.ducvo.crawler.atheahealth.config.AthenahealthRestTemplate;
import asia.ducvo.crawler.atheahealth.domain.AthenahealthPatient;
import asia.ducvo.crawler.atheahealth.domain.PatientSearchResponse;
import asia.ducvo.crawler.atheahealth.repository.AthenahealthPatientRepository;
import asia.ducvo.crawler.atheahealth.utils.AthenahealthDateTimeUtils;
import java.time.LocalDate;
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
public class AthenahealthPatientService {

  private final AthenahealthRestTemplate restTemplate;

  private final AthenahealthPatientRepository patientRepository;

  @GetMapping("/patients")
  public void patients(String practiceId, LocalDate date) {
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
        .pathSegment("v1", practiceId, "patients")
        .queryParam("dob", AthenahealthDateTimeUtils.formatDate(date));

    List<AthenahealthPatient> patients = new ArrayList<>();

    String uri = uriBuilder.build(false).toUriString();
    while (true) {
      ResponseEntity<PatientSearchResponse> response = restTemplate
          .getForEntity(uri, PatientSearchResponse.class);

      PatientSearchResponse body = response.getBody();
      if (body != null) {
        patients.addAll(body.getPatients().stream()
            .peek(i -> i.setPracticeId(practiceId))
            .toList());
      }

      if (body == null || body.getNext() == null || body.getNext().isEmpty()) {
        break;
      }

      UriComponents next = UriComponentsBuilder.fromUriString(body.getNext()).build();
      uri = uriBuilder.replaceQueryParam("offset", next.getQueryParams().get("offset")).build(false).toUriString();
    }

    patientRepository.saveAll(patients);

  }
}
