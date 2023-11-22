package asia.ducvo.crawler.task;

import asia.ducvo.crawler.atheahealth.api.AthenahealthFHIRObservationService;
import asia.ducvo.crawler.atheahealth.domain.AthenahealthPatient;
import asia.ducvo.crawler.atheahealth.repository.AthenahealthPatientRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class CrawlObservationAthenahealth implements ApplicationListener<ApplicationReadyEvent> {

  private final AthenahealthFHIRObservationService observationService;

  private final AthenahealthPatientRepository patientRepository;
  private final ExecutorService executor;

  public CrawlObservationAthenahealth(AthenahealthFHIRObservationService observationService, AthenahealthPatientRepository patientRepository) {
    this.observationService = observationService;
    this.executor = Executors.newFixedThreadPool(100);
    this.patientRepository =patientRepository;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
//    observationService.observations("195900", "1" , "1");
    Page<AthenahealthPatient> page = patientRepository.findByDepartmentIdIsNotNullOrderByPatientId(PageRequest.of(0, 100));

    List<Future<?>> arrays = new ArrayList<>();
    while (true){
      for(AthenahealthPatient patient : page.toList()){
        arrays.add(
            executor.submit(() -> observationService.observations(patient.getPracticeId(), patient.getPatientId(), patient.getDepartmentId()))
        );
      }

      while (arrays.size() > 200) {
        List<Future<?>> lives = new ArrayList<>();
        for (int j = 1; j < arrays.size(); j++) {
          if ((!arrays.get(j).isDone()) && (!arrays.get(j).isCancelled())) {
            lives.add(arrays.get(j));
          }
        }

        arrays = lives;
      }

      if(page.hasNext()){
        page = patientRepository.findByDepartmentIdIsNotNullOrderByPatientId(page.nextPageable());
      } else {
        break;
      }
    }


  }
}
