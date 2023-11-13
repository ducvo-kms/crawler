package asia.ducvo.crawler.task;

import asia.ducvo.crawler.atheahealth.api.AthenahealthPatientService;
import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    while (start.isBefore(LocalDate.now())) {
        LocalDate date = start;
        executor.submit(() -> athenahealthPatientService.patients(practiceId, date));
        start = start.plusDays(1);
    }
  }
}
