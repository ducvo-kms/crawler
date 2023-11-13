package asia.ducvo.crawler.task;

import asia.ducvo.crawler.atheahealth.api.AthenahealthVaccineService;
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
public class CrawlVaccineAthenahealth implements ApplicationListener<ApplicationReadyEvent> {

  private final AthenahealthVaccineService vaccineService;

  private final AthenahealthPatientRepository patientRepository;
  private final ExecutorService executor;

  public CrawlVaccineAthenahealth(AthenahealthVaccineService vaccineService, AthenahealthPatientRepository patientRepository) {
    this.vaccineService = vaccineService;
    this.executor = Executors.newFixedThreadPool(1);
    this.patientRepository =patientRepository;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    Page<AthenahealthPatient> page = patientRepository.findByDepartmentIdIsNotNull(PageRequest.of(0, 100));


    while (true){
      for(AthenahealthPatient patient : page.toList()){
        executor.submit(() -> vaccineService.vaccines(patient.getPracticeId(), patient.getPatientId(), patient.getDepartmentId()));
      }

      if(page.hasNext()){
        page = patientRepository.findByDepartmentIdIsNotNull(page.nextPageable());
      } else {
        break;
      }
    }
  }
}
