package boo.ecrodrigues.user.infrastructure.comment;

import boo.ecrodrigues.user.IntegrationTest;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.comment.Comment;
import boo.ecrodrigues.user.domain.comment.CommentID;
import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import boo.ecrodrigues.user.infrastructure.comment.persistence.CommentEntity;
import boo.ecrodrigues.user.infrastructure.comment.persistence.CommentRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class CommentMongoGatewayTest {

  @Autowired
  private CommentMongoGateway commentGateway;

  @Autowired
  private CommentRepository commentRepository;

  @Test
  public void testDependencies() {
    Assertions.assertNotNull(commentGateway);
    Assertions.assertNotNull(commentRepository);
  }

  @Test
  public void givenAValidComment_whenCallsCreate_shouldPersistIt() {
    // given
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);
    final var expectedId = aComment.getId();

    Assertions.assertEquals(0, commentRepository.count());

    // when
    final var actualComment = commentGateway.create(Comment.with(aComment));

    // then
    Assertions.assertEquals(1, commentRepository.count());

    Assertions.assertEquals(expectedId, actualComment.getId());
    Assertions.assertEquals(expectedTitle, actualComment.getTitle());
    Assertions.assertEquals(expectedComment, actualComment.getComment());
    Assertions.assertEquals(aComment.getCreatedAt(), actualComment.getCreatedAt());
    Assertions.assertEquals(aComment.getUpdatedAt(), actualComment.getUpdatedAt());

    final var persistedComment = commentRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedId.getValue(), persistedComment.getId());
    Assertions.assertEquals(expectedTitle, persistedComment.getTitle());
    Assertions.assertEquals(expectedComment, persistedComment.getComment());
    Assertions.assertEquals(aComment.getCreatedAt(), persistedComment.getCreatedAt());
    Assertions.assertEquals(aComment.getUpdatedAt(), persistedComment.getUpdatedAt());
  }

  @Test
  public void givenAValidComment_whenCallsUpdate_shouldRefreshIt() {
    // given
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);
    final var expectedId = aComment.getId();

    final var currentComment = commentRepository.save(CommentEntity.from(aComment));

    Assertions.assertEquals(1, commentRepository.count());
    Assertions.assertEquals(expectedTitle, currentComment.getTitle());
    Assertions.assertEquals(expectedComment, currentComment.getComment());

    // when
    final var actualComment = commentGateway.update(
        Comment.with(aComment).update(MBTI.ENTJ, Enneagram.E_4W3, Zodiac.Gemini)
    );

    // then
    Assertions.assertEquals(1, commentRepository.count());

    Assertions.assertEquals(expectedId, actualComment.getId());
    Assertions.assertEquals(expectedTitle, actualComment.getTitle());
    Assertions.assertEquals(expectedComment, actualComment.getComment());
    Assertions.assertEquals(aComment.getCreatedAt(), actualComment.getCreatedAt());
    Assertions.assertTrue(aComment.getUpdatedAt().isBefore(actualComment.getUpdatedAt()));

    final var persistedComment = commentRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedId.getValue(), persistedComment.getId());
    Assertions.assertEquals(expectedTitle, persistedComment.getTitle());
    Assertions.assertEquals(expectedComment, persistedComment.getComment());
    Assertions.assertEquals(MBTI.ENTJ, persistedComment.getMbti());
    Assertions.assertEquals(Enneagram.E_4W3, persistedComment.getEnneagram());
    Assertions.assertEquals(Zodiac.Gemini, persistedComment.getZodiac());
    Assertions.assertEquals(aComment.getCreatedAt(), persistedComment.getCreatedAt());
    Assertions.assertTrue(aComment.getUpdatedAt().isBefore(persistedComment.getUpdatedAt()));
  }

  @Test
  public void givenAValidComment_whenCallsFindById_shouldReturnIt() {
    // given
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    final var expectedItems = 1;
    final var expectedId = aComment.getId();

    commentRepository.save(CommentEntity.from(aComment));

    Assertions.assertEquals(1, commentRepository.count());

    // when
    final var actualComment = commentGateway.findById(expectedId).get();

    // then
    Assertions.assertEquals(expectedId.getValue(), actualComment.getId().getValue());
  }
}
