package boo.ecrodrigues.user.domain.comment;

import boo.ecrodrigues.user.domain.UnitTest;
import boo.ecrodrigues.user.domain.account.AccountID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommentTest  extends UnitTest {

  @Test
  public void givenAValidParams_whenCallsNewComment_thenInstantiateAComment() {
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";

    final var actualComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    Assertions.assertNotNull(actualComment);
    Assertions.assertNotNull(actualComment.getId());
    Assertions.assertEquals(expectedAccountID, actualComment.getAccountId());
    Assertions.assertEquals(expectedTitle, actualComment.getTitle());
    Assertions.assertEquals(expectedComment, actualComment.getComment());
    Assertions.assertNotNull(actualComment.getCreatedAt());
    Assertions.assertNotNull(actualComment.getUpdatedAt());
  }

  @Test
  public void givenAValidParams_whenCallsUpdateComment_thenInstantiateAComment() {
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";
    final var expectedMbti = MBTI.ENTP;
    final var expectedEnneagram = Enneagram.E_4W3;
    final var expectedZodiac = Zodiac.Gemini;

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    Assertions.assertNotNull(aComment);
    Assertions.assertNotNull(aComment.getId());
    Assertions.assertEquals(expectedAccountID, aComment.getAccountId());
    Assertions.assertEquals(expectedTitle, aComment.getTitle());
    Assertions.assertEquals(expectedComment, aComment.getComment());
    Assertions.assertNotNull(aComment.getCreatedAt());
    Assertions.assertNotNull(aComment.getUpdatedAt());
    Assertions.assertNull(aComment.getMbti());
    Assertions.assertNull(aComment.getEnneagram());
    Assertions.assertNull(aComment.getZodiac());

    final var actualComment = aComment.update(expectedMbti, expectedEnneagram, expectedZodiac);

    Assertions.assertNotNull(aComment.getMbti());
    Assertions.assertNotNull(aComment.getEnneagram());
    Assertions.assertNotNull(aComment.getZodiac());
    Assertions.assertEquals(expectedMbti, aComment.getMbti());
    Assertions.assertEquals(expectedEnneagram, aComment.getEnneagram());
    Assertions.assertEquals(expectedZodiac, aComment.getZodiac());
  }

  @Test
  public void givenAValidParams_whenCallsUpdateLikeComment_thenInstantiateAComment() {
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";
    final var expectedMbti = MBTI.ENTP;
    final var expectedEnneagram = Enneagram.E_4W3;
    final var expectedZodiac = Zodiac.Gemini;
    final var expectedLike = 1;

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    Assertions.assertNotNull(aComment);
    Assertions.assertNotNull(aComment.getId());
    Assertions.assertEquals(expectedAccountID, aComment.getAccountId());
    Assertions.assertEquals(expectedTitle, aComment.getTitle());
    Assertions.assertEquals(expectedComment, aComment.getComment());
    Assertions.assertNotNull(aComment.getCreatedAt());
    Assertions.assertNotNull(aComment.getUpdatedAt());
    Assertions.assertNull(aComment.getMbti());
    Assertions.assertNull(aComment.getEnneagram());
    Assertions.assertNull(aComment.getZodiac());

    final var actualComment = aComment.update(expectedMbti, expectedEnneagram, expectedZodiac);

    Assertions.assertNotNull(aComment.getMbti());
    Assertions.assertNotNull(aComment.getEnneagram());
    Assertions.assertNotNull(aComment.getZodiac());
    Assertions.assertEquals(expectedMbti, aComment.getMbti());
    Assertions.assertEquals(expectedEnneagram, aComment.getEnneagram());
    Assertions.assertEquals(expectedZodiac, aComment.getZodiac());

    Assertions.assertEquals(0, aComment.getLike());

    final var actualLikeComment = aComment.update(expectedLike);

    Assertions.assertEquals(expectedLike, aComment.getLike());
  }

  @Test
  public void givenAValidParams_whenCallsLikeMethod_thenShouldBeIncreaseALikeOnComment() {
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";
    final var expectedLike = 1;

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    Assertions.assertNotNull(aComment);
    Assertions.assertNotNull(aComment.getId());
    Assertions.assertEquals(expectedAccountID, aComment.getAccountId());
    Assertions.assertEquals(expectedTitle, aComment.getTitle());
    Assertions.assertEquals(expectedComment, aComment.getComment());
    Assertions.assertNotNull(aComment.getCreatedAt());
    Assertions.assertNotNull(aComment.getUpdatedAt());
    Assertions.assertNull(aComment.getMbti());
    Assertions.assertNull(aComment.getEnneagram());
    Assertions.assertNull(aComment.getZodiac());
    Assertions.assertEquals(0, aComment.getLike());

    final var actualComment = aComment.like();

    Assertions.assertEquals(expectedLike, aComment.getLike());
  }

  @Test
  public void givenAValidParams_whenCallsLikeMethod_thenShouldBeDecreaseADislikeOnCommentIfZeroDoesNothing() {
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";
    final var expectedLike = 0;

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    Assertions.assertNotNull(aComment);
    Assertions.assertNotNull(aComment.getId());
    Assertions.assertEquals(expectedAccountID, aComment.getAccountId());
    Assertions.assertEquals(expectedTitle, aComment.getTitle());
    Assertions.assertEquals(expectedComment, aComment.getComment());
    Assertions.assertNotNull(aComment.getCreatedAt());
    Assertions.assertNotNull(aComment.getUpdatedAt());
    Assertions.assertNull(aComment.getMbti());
    Assertions.assertNull(aComment.getEnneagram());
    Assertions.assertNull(aComment.getZodiac());
    Assertions.assertEquals(0, aComment.getLike());

    final var actualComment = aComment.dislike();

    Assertions.assertEquals(expectedLike, actualComment.getLike());
  }
}
