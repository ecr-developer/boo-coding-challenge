package boo.ecrodrigues.user.e2e.comment;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import boo.ecrodrigues.user.E2ETest;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import boo.ecrodrigues.user.e2e.MockDsl;
import boo.ecrodrigues.user.infrastructure.comment.persistence.CommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@E2ETest
@Testcontainers
public class CommentE2ETest implements MockDsl {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private CommentRepository commentRepository;

  @Container
  private static final MongoDBContainer MONGO_CONTAINER = new MongoDBContainer("mongo:4.4.20");

  @DynamicPropertySource
  public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
    registry.add("mongodb.port", () -> MONGO_CONTAINER.getMappedPort(27017));
  }

  @Override
  public MockMvc mvc() {
    return this.mvc;
  }

  @Test
  public void asAnUserIShouldBeAbleToCreateANewCommentWithValidValues() throws Exception {
    Assertions.assertTrue(MONGO_CONTAINER.isRunning());
    Assertions.assertEquals(0, commentRepository.count());

    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";
    final var expectedMbti = MBTI.ESTJ;
    final var expectedEnneagram = Enneagram.E_4W3;
    final var expectedZodiac = Zodiac.Gemini;

    final var actualCommentId = givenAComment(
        expectedAccountID.getValue(),
        expectedTitle,
        expectedComment,
        expectedMbti,
        expectedEnneagram,
        expectedZodiac
    );

    final var actualComment = commentRepository.findById(actualCommentId.getValue()).get();

    Assertions.assertEquals(expectedAccountID.getValue(), actualComment.getAccountId());
    Assertions.assertEquals(expectedTitle, actualComment.getTitle());
    Assertions.assertEquals(expectedComment, actualComment.getComment());
    Assertions.assertNotNull(actualComment.getCreatedAt());
    Assertions.assertNotNull(actualComment.getUpdatedAt());
    Assertions.assertEquals(actualComment.getCreatedAt(), actualComment.getUpdatedAt());
  }

  @Test
  public void asAnUserIShouldBeAbleToNavigateThruAllComments() throws Exception {
    Assertions.assertTrue(MONGO_CONTAINER.isRunning());
    Assertions.assertEquals(0, commentRepository.count());

    givenAComment(
        AccountID.unique().getValue(),
        "Title Celebrety test",
        "Comment Celebrety test ...",
        MBTI.ESTJ,
        Enneagram.E_4W3,
        Zodiac.Gemini
    );
    givenAComment(
        AccountID.unique().getValue(),
        "Title Celebrety 2 test",
        "Comment Celebrety 2 test ...",
        MBTI.ENTJ,
        Enneagram.E_3W2,
        Zodiac.Aquarius
    );
    givenAComment(
        AccountID.unique().getValue(),
        "Title Celebrety 3 test",
        "Comment Celebrety 3 test ...",
        MBTI.ENTP,
        Enneagram.E_1W2,
        Zodiac.Gemini
    );

    listComments(0, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(0)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(3)))
        .andExpect(jsonPath("$.items[0].title", equalTo("Title Celebrety test")));

    listComments(1, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(1)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(3)))
        .andExpect(jsonPath("$.items[1].title", equalTo("Title Celebrety 2 test")));

    listComments(2, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(2)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(3)))
        .andExpect(jsonPath("$.items[2].title", equalTo("Title Celebrety 3 test")));

    listComments(3, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(3)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(6)))
        .andExpect(jsonPath("$.items", hasSize(3)));
  }
}
