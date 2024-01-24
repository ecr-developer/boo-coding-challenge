package boo.ecrodrigues.user.application.comment.create;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import boo.ecrodrigues.user.application.UseCaseTest;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.comment.CommentGateway;
import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import boo.ecrodrigues.user.domain.exceptions.NotificationException;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class CreateCommentUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DefaultCreateCommentUseCase useCase;

  @Mock
  private CommentGateway commentGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(commentGateway);
  }

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

    when(commentGateway.create(any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualOutput = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    verify(commentGateway).create(argThat(aComment ->
        Objects.nonNull(aComment.getId())
            && Objects.equals(expectedTitle, aComment.getTitle())
            && Objects.equals(expectedComment, aComment.getComment())
            && Objects.nonNull(aComment.getCreatedAt())
            && Objects.nonNull(aComment.getUpdatedAt())
    ));
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
}
