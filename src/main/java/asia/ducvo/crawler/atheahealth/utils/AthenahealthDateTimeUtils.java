package asia.ducvo.crawler.atheahealth.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AthenahealthDateTimeUtils {
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

  public static String formatDate(LocalDate date){
    return DATE_TIME_FORMATTER.format(date);
  }
}
