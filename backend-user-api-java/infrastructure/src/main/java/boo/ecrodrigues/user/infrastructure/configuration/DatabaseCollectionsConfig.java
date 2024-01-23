package boo.ecrodrigues.user.infrastructure.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration("dbCollections")
@ConfigurationProperties(prefix = "database.collections")
public class DatabaseCollectionsConfig {

  private String account;

  private String comment;

  public String getAccount() {
    return account;
  }

  public void setAccount(final String account) {
    this.account = account;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
