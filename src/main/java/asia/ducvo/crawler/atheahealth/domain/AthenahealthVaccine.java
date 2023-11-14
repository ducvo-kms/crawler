package asia.ducvo.crawler.atheahealth.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(IdVaccine.class)
public class AthenahealthVaccine implements Serializable {
  @Id
  private String practiceId;

  private String patientId;

  private String departmentId;

  @JsonProperty("ordereddate")
  private String orderedDate;

  @JsonProperty("status")
  private String status;

  @JsonProperty("administerroute")
  private String administerRoute;

  @JsonProperty("visgivendate")
  private String visGivenDate;

  @JdbcTypeCode(SqlTypes.JSON)
  @JsonProperty("vaccineinformationstatements")
  private VaccineInformationStatement[] vaccineInformationStatements;

  @JsonProperty("administerdate")
  private String administerDate;

  @JsonProperty("mvx")
  private String mvx;

  @JsonProperty("vaccinetype")
  private String vaccineType;

  @JdbcTypeCode(SqlTypes.JSON)
  @JsonProperty("clinicalorderclasses")
  private ClinicalOrderClass[] clinicalOrderClasses;

  @JsonProperty("orderedby")
  private String orderedBy;

  @JsonProperty("administersite")
  private String administerSite;

  @JsonProperty("enteredby")
  private String enteredBy;

  @JsonProperty("approvedby")
  private String approvedBy;

  @JsonProperty("cvx")
  private String cvx;

  private String ndc;

  private String units;

  @Id
  @JsonProperty("vaccineid")
  private String vaccineId;

  @JsonProperty("administerroutedescription")
  private String administerRouteDescription;

  @JsonProperty("description")
  private String description;

  @JsonProperty("lotnumber")
  private String lotNumber;

  @JsonProperty("approveddate")
  private String approvedDate;

  @JsonProperty("entereddate")
  private String enteredDate;

  @JsonProperty("partiallyadministered")
  private boolean partiallyAdministered;

  @JsonProperty("genusname")
  private String genusName;

  @JsonProperty("vaccinator")
  private String vaccinator;

  // Getters and Setters for the above fields
}

