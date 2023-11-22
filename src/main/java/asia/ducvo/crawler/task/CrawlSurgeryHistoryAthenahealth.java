package asia.ducvo.crawler.task;

import asia.ducvo.crawler.atheahealth.api.AthenahealthSurgeryHistoryService;
import asia.ducvo.crawler.atheahealth.domain.AthenahealthPatient;
import asia.ducvo.crawler.atheahealth.repository.AthenahealthPatientRepository;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


public class CrawlSurgeryHistoryAthenahealth implements ApplicationListener<ApplicationReadyEvent> {

  private final AthenahealthSurgeryHistoryService surgeryHistoryService;

  private final AthenahealthPatientRepository patientRepository;
  private final ExecutorService executor;

  public CrawlSurgeryHistoryAthenahealth(AthenahealthSurgeryHistoryService surgeryHistoryService, AthenahealthPatientRepository patientRepository) {
    this.surgeryHistoryService = surgeryHistoryService;
    this.executor = Executors.newFixedThreadPool(100);
    this.patientRepository =patientRepository;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    Page<AthenahealthPatient> page = patientRepository.findByDepartmentIdIsNotNullOrderByPatientId(PageRequest.of(0, 100));


    while (true){
      for(AthenahealthPatient patient : page.toList()){
        executor.submit(() -> surgeryHistoryService.procedures(patient.getPracticeId(), patient.getPatientId(), patient.getDepartmentId()));
      }

      if(page.hasNext()){
        page = patientRepository.findByDepartmentIdIsNotNullOrderByPatientId(page.nextPageable());
      } else {
        break;
      }
    }


  }
}
