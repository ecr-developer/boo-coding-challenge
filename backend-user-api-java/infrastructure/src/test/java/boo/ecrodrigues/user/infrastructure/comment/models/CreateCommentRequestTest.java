package boo.ecrodrigues.user.infrastructure.comment.models;

import boo.ecrodrigues.user.JacksonTest;
import boo.ecrodrigues.user.domain.account.AccountID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class CreateCommentRequestTest {

  @Autowired
  private JacksonTester<CreateCommentRequest> json;

  @Test
  public void testUnmarshall() throws Exception {
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";

    final var json = """
                {
                  "accountId": "%s",
                  "title": "%s",
                  "comment": "%s"
                }
                """.formatted(expectedAccountID.getValue(), expectedTitle, expectedComment);

    final var actualJson = this.json.parse(json);

    Assertions.assertThat(actualJson)
        .hasFieldOrPropertyWithValue("accountId", expectedAccountID.getValue())
        .hasFieldOrPropertyWithValue("title", expectedTitle)
        .hasFieldOrPropertyWithValue("comment", expectedComment);
  }
}
