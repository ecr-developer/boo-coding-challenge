package boo.ecrodrigues.user.infrastructure.account.models;

import boo.ecrodrigues.user.JacksonTest;
import java.time.Instant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class AccountResponseTest {

  @Autowired
  private JacksonTester<AccountResponse> json;

  @Test
  public void testMarshall() throws Exception {
    final var expectedId = "123";
    final var expectedName = "A Martinez";
    final var expectedIsActive = false;
    final var expectedCreatedAt = Instant.now();
    final var expectedUpdatedAt = Instant.now();
    final var expectedDeletedAt = Instant.now();

    final var response = new AccountResponse(
        expectedId,
        expectedName,
        expectedIsActive,
        expectedCreatedAt,
        expectedUpdatedAt,
        expectedDeletedAt
    );

    final var actualJson = this.json.write(response);

    Assertions.assertThat(actualJson)
        .hasJsonPathValue("$.id", expectedId)
        .hasJsonPathValue("$.name", expectedName)
        .hasJsonPathValue("$.is_active", expectedIsActive)
        .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
        .hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString())
        .hasJsonPathValue("$.updated_at", expectedUpdatedAt.toString());
  }

  @Test
  public void testUnmarshall() throws Exception {
    final var expectedId = "123";
    final var expectedName = "A Martinez";
    final var expectedIsActive = false;
    final var expectedCreatedAt = Instant.now();
    final var expectedUpdatedAt = Instant.now();
    final var expectedDeletedAt = Instant.now();

    final var json = """
        {
          "id": "%s",
          "name": "%s",
          "is_active": %s,
          "created_at": "%s",
          "deleted_at": "%s",
          "updated_at": "%s"
        }    
        """.formatted(
        expectedId,
        expectedName,
        expectedIsActive,
        expectedCreatedAt.toString(),
        expectedDeletedAt.toString(),
        expectedUpdatedAt.toString()
    );

    final var actualJson = this.json.parse(json);

    Assertions.assertThat(actualJson)
        .hasFieldOrPropertyWithValue("id", expectedId)
        .hasFieldOrPropertyWithValue("name", expectedName)
        .hasFieldOrPropertyWithValue("active", expectedIsActive)
        .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
        .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt)
        .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt);
  }
}
