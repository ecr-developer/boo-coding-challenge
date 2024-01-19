package boo.ecrodrigues.user.infrastructure.account.models;

import boo.ecrodrigues.user.JacksonTest;
import java.time.Instant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class AccountListResponseTest {

  @Autowired
  private JacksonTester<AccountListResponse> json;

  @Test
  public void testMarshall() throws Exception {
    final var expectedId = "123";
    final var expectedName = "A Martinez";
    final var expectedIsActive = false;
    final var expectedCreatedAt = Instant.now();
    final var expectedDeletedAt = Instant.now();

    final var response = new AccountListResponse(
        expectedId,
        expectedName,
        expectedIsActive,
        expectedCreatedAt,
        expectedDeletedAt
    );

    final var actualJson = this.json.write(response);

    Assertions.assertThat(actualJson)
        .hasJsonPathValue("$.id", expectedId)
        .hasJsonPathValue("$.name", expectedName)
        .hasJsonPathValue("$.is_active", expectedIsActive)
        .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
        .hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString());
  }
}
