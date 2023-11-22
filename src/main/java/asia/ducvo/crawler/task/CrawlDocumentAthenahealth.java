package asia.ducvo.crawler.task;

import asia.ducvo.crawler.atheahealth.api.AthenahealthDocumentService;
import asia.ducvo.crawler.atheahealth.domain.AthenahealthPatient;
import asia.ducvo.crawler.atheahealth.repository.AthenahealthPatientRepository;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class CrawlDocumentAthenahealth implements ApplicationListener<ApplicationReadyEvent> {

  private final AthenahealthDocumentService documentService;

  private final AthenahealthPatientRepository patientRepository;
  private final ExecutorService executor;

  public CrawlDocumentAthenahealth(AthenahealthDocumentService documentService, AthenahealthPatientRepository patientRepository) {
    this.documentService = documentService;
    this.executor = Executors.newFixedThreadPool(100);
    this.patientRepository =patientRepository;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    Page<AthenahealthPatient> page = patientRepository.findByDepartmentIdIsNotNullOrderByPatientId(PageRequest.of(0, 100));


    while (true){
      for(AthenahealthPatient patient : page.toList()){
        executor.submit(() -> documentService.documents(patient.getPracticeId(), patient.getPatientId(), patient.getDepartmentId()));
      }

      if(page.hasNext()){
        page = patientRepository.findByDepartmentIdIsNotNullOrderByPatientId(page.nextPageable());
      } else {
        break;
      }
    }


  }
}
