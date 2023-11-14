package asia.ducvo.crawler.task;

import asia.ducvo.crawler.atheahealth.api.AthenahealthDocumentService;
import asia.ducvo.crawler.atheahealth.api.AthenahealthEncounterService;
import asia.ducvo.crawler.atheahealth.domain.AthenahealthPatient;
import asia.ducvo.crawler.atheahealth.repository.AthenahealthPatientRepository;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;


@Component
public class CrawlEncounterAthenahealth implements ApplicationListener<ApplicationReadyEvent> {

  private final AthenahealthEncounterService encounterService;

  private final AthenahealthPatientRepository patientRepository;
  private final ExecutorService executor;

  public CrawlEncounterAthenahealth(AthenahealthEncounterService encounterService, AthenahealthPatientRepository patientRepository) {
    this.encounterService = encounterService;
    this.executor = Executors.newFixedThreadPool(100);
    this.patientRepository =patientRepository;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    Page<AthenahealthPatient> page = patientRepository.findByDepartmentIdIsNotNull(PageRequest.of(0, 100));


    while (true){
      for(AthenahealthPatient patient : page.toList()){
        executor.submit(() -> encounterService.encounters(patient.getPracticeId(), patient.getPatientId(), patient.getDepartmentId()));
      }

      if(page.hasNext()){
        page = patientRepository.findByDepartmentIdIsNotNull(page.nextPageable());
      } else {
        break;
      }
    }


  }
}
