package boo.ecrodrigues.user.infrastructure.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration("dbCollections")
@ConfigurationProperties(prefix = "database.collections")
public class DatabaseCollectionsConfig {

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  private String account;
}
