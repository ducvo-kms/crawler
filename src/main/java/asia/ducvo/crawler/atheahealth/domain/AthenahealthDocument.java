package asia.ducvo.crawler.atheahealth.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Table(name = "athenahealth_document_2")
public class AthenahealthDocument {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String practiceId;

  @JsonProperty("adminid")
  private int adminId;

  @JsonProperty("patientid")
  private int patientId;

  @JsonProperty("documentdescription")
  private String documentDescription;

  @JsonProperty("status")
  private String status;

  @JsonProperty("documentclass")
  private String documentClass;

  @JsonProperty("priority")
  private int priority;

  @JsonProperty("documenttype")
  private String documentType;

  @JsonProperty("assignedto")
  private String assignedTo;

  @JsonProperty("documentid")
  private String documentId;

  @JsonProperty("documentroute")
  private String documentRoute;

  @JsonProperty("lastmodifieddatetime")
  private String lastModifiedDateTime;

  @JsonProperty("originalfilename")
  private String originalFileName;

  @JsonProperty("createddatetime")
  private String createdDateTime;

  @JsonProperty("createddate")
  private String createdDate;

  @JsonProperty("lastmodifieduser")
  private String lastModifiedUser;

  @JsonProperty("filesize")
  private int fileSize;

  @JsonProperty("createduser")
  private String createdUser;

  @JsonProperty("documentsubclass")
  private String documentSubclass;

  @JsonProperty("documentsource")
  private String documentSource;
}
