package boo.ecrodrigues.user.infrastructure.comment.models;

import boo.ecrodrigues.user.JacksonTest;
import boo.ecrodrigues.user.domain.comment.CommentID;
import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class UpdateCommentRequestTest {

  @Autowired
  private JacksonTester<UpdateCommentRequest> json;

  @Test
  public void testUnmarshall() throws Exception {
    final var expectedId = CommentID.unique();
    final var expectedMbti = MBTI.ENTJ;
    final var expectedEnneagram = Enneagram.E_4W3;
    final var expectedZodiac = Zodiac.Gemini;
    final var expectedLike = 1;

    final var json = """
                {
                  "id": "%s",
                  "mbti": "%s",
                  "enneagram": "%s",
                  "zodiac": "%s",
                  "like": "%s"
                }
                """.formatted(expectedId.getValue(), expectedMbti, expectedEnneagram, expectedZodiac, expectedLike);

    final var actualJson = this.json.parse(json);

    Assertions.assertThat(actualJson)
        .hasFieldOrPropertyWithValue("id", expectedId.getValue())
        .hasFieldOrPropertyWithValue("mbti", expectedMbti)
        .hasFieldOrPropertyWithValue("enneagram", expectedEnneagram)
        .hasFieldOrPropertyWithValue("zodiac", expectedZodiac)
        .hasFieldOrPropertyWithValue("like", expectedLike);
  }
}
