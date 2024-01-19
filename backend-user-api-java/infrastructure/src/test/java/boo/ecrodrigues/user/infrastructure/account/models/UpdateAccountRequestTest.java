package boo.ecrodrigues.user.infrastructure.account.models;

import boo.ecrodrigues.user.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class UpdateAccountRequestTest {

  @Autowired
  private JacksonTester<UpdateAccountRequest> json;

  @Test
  public void testUnmarshall() throws Exception {
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    final var json = """
        {
          "name": "%s",
          "is_active": %s
        }    
        """.formatted(expectedName, expectedIsActive);

    final var actualJson = this.json.parse(json);

    Assertions.assertThat(actualJson)
        .hasFieldOrPropertyWithValue("name", expectedName)
        .hasFieldOrPropertyWithValue("active", expectedIsActive);
  }
}
