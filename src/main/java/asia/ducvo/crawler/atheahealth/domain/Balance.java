package asia.ducvo.crawler.atheahealth.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Balance {
  @JsonProperty("departmentlist")
  private String departmentList;

  @JsonProperty("balance")
  private String balance;

  @JsonProperty("cleanbalance")
  private boolean cleanBalance;

  @JsonProperty("providergroupid")
  private String providerGroupId;
}