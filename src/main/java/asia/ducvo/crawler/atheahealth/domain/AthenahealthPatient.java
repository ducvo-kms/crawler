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
@IdClass(IdPatient.class)
public class AthenahealthPatient implements Serializable {
  @Id
  private String practiceId;

  private String email;

  @JsonProperty("guarantorcountrycode3166")
  private String guarantorCountryCode3166;

  private String city;

  @JsonProperty("departmentid")
  private String departmentId;

  @JsonProperty("contactpreference")
  private String contactPreference;

  @JsonProperty("portaltermsonfile")
  private boolean portalTermsOnFile;

  @JsonProperty("consenttotext")
  private boolean consentToText;

  @JsonProperty("ethnicitycode")
  private String ethnicityCode;

  private String dob;

  @JsonProperty("patientphoto")
  private boolean patientPhoto;

  @JsonProperty("guarantorzip")
  private String guarantorZip;

  @JsonProperty("guarantorfirstname")
  private String guarantorFirstName;

  @JsonProperty("consenttocall")
  private boolean consentToCall;

  private String lastname;

  @JsonProperty("racename")
  private String raceName;

  @JsonProperty("guarantorcity")
  private String guarantorCity;

  @JsonProperty("guarantorlastname")
  private String guarantorLastName;

  private String zip;

  @JsonProperty("guarantordob")
  private String guarantorDob;

  private String[] ethnicityCodes;

  @JsonProperty("hierarchicalcode")
  private String hierarchicalCode;

  @JsonProperty("guarantorrelationshiptopatient")
  private String guarantorRelationshipToPatient;

  @JsonProperty("povertylevelfamilysizedeclined")
  private boolean povertyLevelFamilySizeDeclined;

  private String firstname;

  @JsonProperty("confidentialitycode")
  private String confidentialityCode;

  @JsonProperty("guarantoraddress1")
  private String guarantorAddress1;

  @JsonProperty("emailexists")
  private boolean emailExists;

  private String sex;

  private String homephone;

  @JsonProperty("guarantorstate")
  private String guarantorState;

  @JsonProperty("firstnameused")
  private String firstNameUsed;

  @JdbcTypeCode(SqlTypes.JSON)
  private Balance[] balances;

  @JsonProperty("guarantoremail")
  private String guarantorEmail;

  @JsonProperty("povertylevelincomedeclined")
  private boolean povertyLevelIncomeDeclined;

  private String mobilephone;

  @Id
  @JsonProperty("patientid")
  private String patientId;

  @JsonProperty("driverslicense")
  private boolean driversLicense;

  private String address1;

  @JsonProperty("primarydepartmentid")
  private String primaryDepartmentId;

  @JsonProperty("guarantoraddresssameaspatient")
  private boolean guarantorAddressSameAsPatient;

  @JsonProperty("guarantorphone")
  private String guarantorPhone;

  @JsonProperty("countrycode")
  private String countryCode;

  @JsonProperty("caresummarydeliverypreference")
  private String careSummaryDeliveryPreference;

  @JsonProperty("registrationdate")
  private String registrationDate;

  @JsonProperty("guarantorcountrycode")
  private String guarantorCountryCode;

  @JsonProperty("language6392code")
  private String language6392Code;

  @JsonProperty("primaryproviderid")
  private String primaryProviderId;

  private String status;

  @JdbcTypeCode(SqlTypes.JSON)
  private String[] race;

  @JsonProperty("privacyinformationverified")
  private boolean privacyInformationVerified;

  private boolean hasmobile;

  @JsonProperty("countrycode3166")
  private String countryCode3166;

  private String state;

  // Add getters and setters here
}