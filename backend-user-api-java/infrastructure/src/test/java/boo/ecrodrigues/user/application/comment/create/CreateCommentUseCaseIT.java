package boo.ecrodrigues.user.application.comment.create;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import boo.ecrodrigues.user.IntegrationTest;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.comment.CommentGateway;
import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import boo.ecrodrigues.user.domain.exceptions.NotificationException;
import boo.ecrodrigues.user.infrastructure.comment.persistence.CommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class CreateCommentUseCaseIT {

  @Autowired
  private CreateCommentUseCase useCase;

  @Autowired
  private CommentRepository commentRepository;

  @SpyBean
  private CommentGateway commentGateway;

  @Test
  public void givenAValidCommand_whenCallsCreateComment_shouldReturnIt() {
    // given
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";
    final var expectedMbti = MBTI.ESTJ;
    final var expectedEnneagram = Enneagram.E_4W3;
    final var expectedZodiac = Zodiac.Gemini;

    final var aCommand = CreateCommentCommand.with(
        expectedAccountID,
        expectedTitle,
        expectedComment,
        expectedMbti,
        expectedEnneagram,
        expectedZodiac
    );

    // when
    final var actualOutput = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    final var actualComment = this.commentRepository.findById(actualOutput.id()).get();

    Assertions.assertEquals(expectedAccountID.getValue(), actualComment.getAccountId());
    Assertions.assertEquals(expectedTitle, actualComment.getTitle());
    Assertions.assertEquals(expectedComment, actualComment.getComment());
    Assertions.assertEquals(expectedMbti, actualComment.getMbti());
    Assertions.assertEquals(expectedEnneagram, actualComment.getEnneagram());
    Assertions.assertEquals(expectedZodiac, actualComment.getZodiac());
    Assertions.assertEquals(0, actualComment.getLike());
    Assertions.assertNotNull(actualComment.getCreatedAt());
    Assertions.assertNotNull(actualComment.getUpdatedAt());

    verify(commentGateway).create(any());
  }

  @Test
  public void givenAInvalidTitle_whenCallsCreateComment_shouldThrowsNotificationException() {
    // given
    final var expectedAccountID = AccountID.unique();
    final String expectedTitle = null;
    final var expectedComment = "Comment Celebrety test ...";
    final var expectedMbti = MBTI.ESTJ;
    final var expectedEnneagram = Enneagram.E_4W3;
    final var expectedZodiac = Zodiac.Gemini;

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'title' should not be null";

    final var aCommand = CreateCommentCommand.with(
        expectedAccountID,
        expectedTitle,
        expectedComment,
        expectedMbti,
        expectedEnneagram,
        expectedZodiac
    );

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(commentGateway, times(0)).create(any());
  }

  @Test
  public void givenAInvalidComment_whenCallsCreateComment_shouldThrowsNotificationException() {
    // given
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final String expectedComment = null;
    final var expectedMbti = MBTI.ESTJ;
    final var expectedEnneagram = Enneagram.E_4W3;
    final var expectedZodiac = Zodiac.Gemini;

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'comment' should not be null";

    final var aCommand = CreateCommentCommand.with(
        expectedAccountID,
        expectedTitle,
        expectedComment,
        expectedMbti,
        expectedEnneagram,
        expectedZodiac
    );

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(commentGateway, times(0)).create(any());
  }

  @Test
  public void givenAInvalidMbti_whenCallsCreateComment_shouldThrowsNotificationException() {
    // given
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";
    //final var expectedMbti = MBTI.ESTJ;
    final var expectedEnneagram = Enneagram.E_4W3;
    final var expectedZodiac = Zodiac.Gemini;

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'mbti' should not be null";

    final var aCommand = CreateCommentCommand.with(
        expectedAccountID,
        expectedTitle,
        expectedComment,
        null,
        expectedEnneagram,
        expectedZodiac
    );

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(commentGateway, times(0)).create(any());
  }

  @Test
  public void givenAInvalidEnneagram_whenCallsCreateComment_shouldThrowsNotificationException() {
    // given
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";
    final var expectedMbti = MBTI.ESTJ;
    //final var expectedEnneagram = Enneagram.E_4W3;
    final var expectedZodiac = Zodiac.Gemini;

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'enneagram' should not be null";

    final var aCommand = CreateCommentCommand.with(
        expectedAccountID,
        expectedTitle,
        expectedComment,
        expectedMbti,
        null,
        expectedZodiac
    );

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(commentGateway, times(0)).create(any());
  }

  @Test
  public void givenAInvalidZodiac_whenCallsCreateComment_shouldThrowsNotificationException() {
    // given
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";
    final var expectedMbti = MBTI.ESTJ;
    final var expectedEnneagram = Enneagram.E_4W3;
    //final var expectedZodiac = Zodiac.Gemini;

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'zodiac' should not be null";

    final var aCommand = CreateCommentCommand.with(
        expectedAccountID,
        expectedTitle,
        expectedComment,
        expectedMbti,
        expectedEnneagram,
        null
    );

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(commentGateway, times(0)).create(any());
  }
}
