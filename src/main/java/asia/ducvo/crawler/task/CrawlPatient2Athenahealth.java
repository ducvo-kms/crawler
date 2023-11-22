package asia.ducvo.crawler.task;

import asia.ducvo.crawler.atheahealth.api.AthenahealthPatientService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

public class CrawlPatient2Athenahealth implements ApplicationListener<ApplicationReadyEvent> {

  private final AthenahealthPatientService athenahealthPatientService;
  private final ExecutorService executor;

  public CrawlPatient2Athenahealth(AthenahealthPatientService athenahealthPatientService) {
    this.athenahealthPatientService = athenahealthPatientService;
    this.executor = Executors.newFixedThreadPool(100);
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    String practiceId = "195900";
    List<Future<?>> arrays = new ArrayList<>();
    for (int i = 0; i < 1_000_000_000; i++) {
      String id = String.valueOf(i);
      arrays.add(executor.submit(() -> athenahealthPatientService.patients(practiceId, id)));


      while (arrays.size() > 100){
        List<Future<?>> lives = new ArrayList<>();
        for(int j = 1; j < arrays.size(); j++){
          if((!arrays.get(j).isDone()) && (!arrays.get(j).isCancelled())){
            lives.add(arrays.get(j));
          }
        }

        arrays = lives;
      }
    }
  }
}
