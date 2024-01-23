package boo.ecrodrigues.user.infrastructure.comment.models;

import boo.ecrodrigues.user.JacksonTest;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.comment.CommentID;
import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import java.time.Instant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class CommentResponseTest {

  @Autowired
  private JacksonTester<CommentResponse> json;

  @Test
  public void testMarshall() throws Exception {
    final var expectedId = CommentID.unique();
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";
    final var expectedMbti = MBTI.ENTJ;
    final var expectedEnneagram = Enneagram.E_4W3;
    final var expectedZodiac = Zodiac.Gemini;
    final var expectedLike = 3;
    final var expectedCreatedAt = Instant.now();
    final var expectedUpdatedAt = Instant.now();

    final var response = new CommentResponse(
        expectedAccountID.getValue(),
        expectedId.getValue(),
        expectedTitle,
        expectedComment,
        expectedMbti,
        expectedEnneagram,
        expectedZodiac,
        expectedLike,
        expectedCreatedAt,
        expectedUpdatedAt
    );

    final var actualJson = this.json.write(response);

    Assertions.assertThat(actualJson)
        .hasJsonPathValue("$.accountId", expectedAccountID.getValue())
        .hasJsonPathValue("$.id", expectedId.getValue())
        .hasJsonPathValue("$.title", expectedTitle)
        .hasJsonPathValue("$.comment", expectedComment)
        .hasJsonPathValue("$.mbti", expectedMbti)
        .hasJsonPathValue("$.enneagram", expectedEnneagram)
        .hasJsonPathValue("$.zodiac", expectedZodiac)
        .hasJsonPathValue("$.like", expectedLike)
        .hasJsonPathValue("$.created_at", expectedCreatedAt)
        .hasJsonPathValue("$.updated_at", expectedUpdatedAt);
  }
}
