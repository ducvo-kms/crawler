package asia.ducvo.crawler.atheahealth.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AthenahealthEncounter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String practiceId;

  @JsonProperty("appointmentstartdate")
  private String appointmentStartDate;

  @JsonProperty("stage")
  private String stage;

  @JsonProperty("status")
  private String status;

  @JsonProperty("encounterdate")
  private String encounterDate;

  @JsonProperty("lastupdated")
  private String lastUpdated;

  @JsonProperty("encountertype")
  private String encounterType;

  @JsonProperty("patientlocation")
  private String patientLocation;

  @JsonProperty("patientstatusid")
  private int patientStatusId;

  @JsonProperty("encountervisitname")
  private String encounterVisitName;

  @JsonProperty("chartid")
  private int chartId;

  @JsonProperty("appointmentid")
  private int appointmentId;

  @JsonProperty("departmentid")
  private int departmentId;

  @JsonProperty("providerid")
  private int providerId;

  @JsonProperty("encounterid")
  private int encounterId;

  @JsonProperty("patientstatus")
  private String patientStatus;

  @JsonProperty("providerlastname")
  private String providerLastName;

  @JsonProperty("patientlocationid")
  private int patientLocationId;

}
