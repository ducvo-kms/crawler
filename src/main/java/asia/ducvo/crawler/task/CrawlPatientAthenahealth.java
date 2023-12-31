package asia.ducvo.crawler.task;

import asia.ducvo.crawler.atheahealth.api.AthenahealthPatientService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


public class CrawlPatientAthenahealth implements ApplicationListener<ApplicationReadyEvent> {

  private final AthenahealthPatientService athenahealthPatientService;
  private final ExecutorService executor;

  public CrawlPatientAthenahealth(AthenahealthPatientService athenahealthPatientService) {
    this.athenahealthPatientService = athenahealthPatientService;
    this.executor = Executors.newFixedThreadPool(100);
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    String practiceId = "195900";
    LocalDate start = LocalDate.parse("1900-01-01");
    List<Future<?>> arrays = new ArrayList<>();
    while (start.isBefore(LocalDate.now())) {
      LocalDate date = start;
      start = start.plusDays(1);
      arrays.add(executor.submit(() -> athenahealthPatientService.patients(practiceId, date)));
      while (arrays.size() > 200) {
        List<Future<?>> lives = new ArrayList<>();
        for (int j = 1; j < arrays.size(); j++) {
          if ((!arrays.get(j).isDone()) && (!arrays.get(j).isCancelled())) {
            lives.add(arrays.get(j));
          }
        }

        arrays = lives;
      }
    }
  }
}
