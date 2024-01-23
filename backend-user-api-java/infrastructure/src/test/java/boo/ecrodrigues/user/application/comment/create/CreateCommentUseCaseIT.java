package boo.ecrodrigues.user.application.comment.create;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import boo.ecrodrigues.user.IntegrationTest;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.comment.CommentGateway;
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

    final var aCommand = CreateCommentCommand.with(expectedAccountID, expectedTitle, expectedComment);

    // when
    final var actualOutput = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    final var actualComment = this.commentRepository.findById(actualOutput.id()).get();

    Assertions.assertEquals(expectedAccountID.getValue(), actualComment.getAccountId());
    Assertions.assertEquals(expectedTitle, actualComment.getTitle());
    Assertions.assertEquals(expectedComment, actualComment.getComment());
    Assertions.assertNull(actualComment.getMbti());
    Assertions.assertNull(actualComment.getEnneagram());
    Assertions.assertNull(actualComment.getZodiac());
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

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'title' should not be null";

    final var aCommand = CreateCommentCommand.with(expectedAccountID, expectedTitle, expectedComment);

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

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'comment' should not be null";

    final var aCommand = CreateCommentCommand.with(expectedAccountID, expectedTitle, expectedComment);

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
