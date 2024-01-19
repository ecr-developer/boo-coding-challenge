package boo.ecrodrigues.user.infrastructure;

import boo.ecrodrigues.user.infrastructure.configuration.WebServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication(scanBasePackages = "boo.ecrodrigues.user")
public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    LOG.info("[step:to-be-init] [id:1] Initializing Spring");
    System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "test-integration");
    SpringApplication.run(WebServerConfig.class, args);
    LOG.info("[step:inittialized] [id:2] Spring inittialized...");
  }
}