package boo.ecrodrigues.user.infrastructure.comment.models;

import boo.ecrodrigues.user.JacksonTest;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
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
    final var expectedMbti = MBTI.ESTJ;
    final var expectedEnneagram = Enneagram.E_4W3;
    final var expectedZodiac = Zodiac.Gemini;

    final var json = """
                {
                  "accountId": "%s",
                  "title": "%s",
                  "comment": "%s",
                  "mbti": "%s",
                  "enneagram": "%s",
                  "zodiac": "%s"
                }
                """.formatted(
                    expectedAccountID.getValue(),
                    expectedTitle,
                    expectedComment,
                    expectedMbti,
                    expectedEnneagram,
                    expectedZodiac
    );

    final var actualJson = this.json.parse(json);

    Assertions.assertThat(actualJson)
        .hasFieldOrPropertyWithValue("accountId", expectedAccountID.getValue())
        .hasFieldOrPropertyWithValue("title", expectedTitle)
        .hasFieldOrPropertyWithValue("comment", expectedComment)
        .hasFieldOrPropertyWithValue("mbti", expectedMbti)
        .hasFieldOrPropertyWithValue("enneagram", expectedEnneagram)
        .hasFieldOrPropertyWithValue("zodiac", expectedZodiac);
  }
}
