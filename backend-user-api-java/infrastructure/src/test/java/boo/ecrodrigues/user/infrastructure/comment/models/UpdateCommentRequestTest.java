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
    final var expectedLike = 1;

    final var json = """
                {
                  "like": "%s"
                }
                """.formatted(expectedLike);

    final var actualJson = this.json.parse(json);

    Assertions.assertThat(actualJson)
        .hasFieldOrPropertyWithValue("like", expectedLike);
  }
}
