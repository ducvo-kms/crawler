package asia.ducvo.crawler.atheahealth.api;

import asia.ducvo.crawler.atheahealth.config.AthenahealthRestTemplate;
import asia.ducvo.crawler.atheahealth.domain.AthenahealthDocument;
import asia.ducvo.crawler.atheahealth.domain.DocumentSearchResponse;
import asia.ducvo.crawler.atheahealth.repository.AthenahealthDocumentRepository;
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
public class AthenahealthDocumentService {

  private final AthenahealthRestTemplate restTemplate;

  private final AthenahealthDocumentRepository documentRepository;

  @GetMapping("/documents")
  public void documents(String practiceId, String patientId, String departmentId) {
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
        .pathSegment("v1", practiceId, "patients", patientId, "documents")
        .queryParam("departmentid", departmentId)
        .queryParam("showdeleted", "true")
        .queryParam("showdeclinedorders", "true")
        .queryParam("showmetadata", "true");

    List<AthenahealthDocument> vaccines = new ArrayList<>();

    String uri = uriBuilder.build(false).toUriString();
    while (true) {
      ResponseEntity<DocumentSearchResponse> response = restTemplate
          .getForEntity(uri, DocumentSearchResponse.class);

      if(!response.getStatusCode().is2xxSuccessful()){
        log.info("Error: {}", response);
      }

      DocumentSearchResponse body = response.getBody();

      log.info("Body: {}", body);

      if (body != null) {
        vaccines.addAll(body.getDocuments().stream()
            .peek(i -> i.setPracticeId(practiceId))
            .toList());
      }

      if (body == null || body.getNext() == null || body.getNext().isEmpty()) {
        break;
      }

      UriComponents next = UriComponentsBuilder.fromUriString(body.getNext()).build();
      uri = uriBuilder.replaceQueryParam("offset", next.getQueryParams().get("offset")).build(false).toUriString();
    }

    documentRepository.saveAll(vaccines);

  }
}
