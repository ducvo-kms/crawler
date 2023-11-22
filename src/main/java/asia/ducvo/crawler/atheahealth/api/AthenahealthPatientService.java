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
import org.springframework.web.client.RestClientException;
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
        .queryParam("dob", AthenahealthDateTimeUtils.formatDate(date))
        .queryParam("limit", 1000);

    List<AthenahealthPatient> patients = new ArrayList<>();

    String uri = uriBuilder.build(false).toUriString();
    while (true) {
      try {
        ResponseEntity<PatientSearchResponse> response = restTemplate
            .getForEntity(uri, PatientSearchResponse.class);

        PatientSearchResponse body = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful()) {
          log.info("Error: {}", response);
        }

        log.info("Body: {}", body);

        if (body != null) {
          patients.addAll(body.getPatients().stream()
              .peek(i -> i.setPracticeId(practiceId))
              .toList());
        }

        if (body == null || body.getNext() == null || body.getNext().isEmpty()) {
          break;
        }

        UriComponents next = UriComponentsBuilder.fromUriString(body.getNext()).build();
        uri = uriBuilder.replaceQueryParam("offset", next.getQueryParams().get("offset"))
            .build(false).toUriString();
      } catch (RestClientException exception) {
        log.info("Error get patients", exception);

        if(exception.getMessage().contains("The given search parameters")){
          for(char i = 'a'; i < 'z' ; i++){
            patientsWithGiven(practiceId, date, String.valueOf(i));
          }
        }

        return;
      }
    }

    patientRepository.saveAll(patients);

  }

  public void patientsWithGiven(String practiceId, LocalDate date, String given) {
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
        .pathSegment("v1", practiceId, "patients")
        .queryParam("given", given)
        .queryParam("dob", AthenahealthDateTimeUtils.formatDate(date))
        .queryParam("limit", 1000);

    List<AthenahealthPatient> patients = new ArrayList<>();

    String uri = uriBuilder.build(false).toUriString();
    while (true) {
      try {
        ResponseEntity<PatientSearchResponse> response = restTemplate
            .getForEntity(uri, PatientSearchResponse.class);

        PatientSearchResponse body = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful()) {
          log.info("Error: {}", response);
        }

        log.info("Body: {}", body);

        if (body != null) {
          patients.addAll(body.getPatients().stream()
              .peek(i -> i.setPracticeId(practiceId))
              .toList());
        }

        if (body == null || body.getNext() == null || body.getNext().isEmpty()) {
          break;
        }

        UriComponents next = UriComponentsBuilder.fromUriString(body.getNext()).build();
        uri = uriBuilder.replaceQueryParam("offset", next.getQueryParams().get("offset"))
            .build(false).toUriString();
      } catch (RestClientException exception) {
        log.info("Error get patients", exception);
      }
    }

    try{
      for(var patient: patients){
        patientRepository.save(patient);
      }
    }catch (Exception ignored){

    }


  }

  @GetMapping("/patients2")
  public void patients(String practiceId, String id) {
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
        .pathSegment("v1", practiceId, "patients", id);

    String uri = uriBuilder.build(false).toUriString();

      try {
        ResponseEntity<AthenahealthPatient[]> response = restTemplate
            .getForEntity(uri, AthenahealthPatient[].class);

        AthenahealthPatient[] body = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful()) {
          log.info("Error: {}", response);
        }

        if(body == null || body.length == 0){
          return;
        }

        AthenahealthPatient patient = body[0];
        patient.setPracticeId(practiceId);

        patientRepository.saveAndFlush(patient);
      } catch (RestClientException exception) {
        log.info("Error get patients", exception);
      }
    }

}
