package boo.ecrodrigues.user.application.comment.update;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import boo.ecrodrigues.user.application.UseCaseTest;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.comment.Comment;
import boo.ecrodrigues.user.domain.comment.CommentGateway;
import boo.ecrodrigues.user.domain.comment.CommentID;
import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import boo.ecrodrigues.user.domain.exceptions.NotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UpdateCommentUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DefaultUpdateCommentUseCase useCase;

  @Mock
  private CommentGateway commentGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(commentGateway);
  }

  @Test
  public void givenAValidCommand_whenCallsUpdateComment_shouldReturnItsIdentifier() {
    // given
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    final var expectedId = aComment.getId();

    final var aCommand = UpdateCommentCommand.with(
        expectedId.getValue(),
        MBTI.ENTJ,
        Enneagram.E_3W4,
        Zodiac.Gemini,
        1
    );

    when(commentGateway.findById(any()))
        .thenReturn(Optional.of(Comment.with(aComment)));

    when(commentGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualOutput = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    verify(commentGateway).findById(eq(expectedId));

    verify(commentGateway).update(argThat(aUpdatedComment ->
        Objects.equals(expectedId, aUpdatedComment.getId())
            && Objects.equals(expectedTitle, aUpdatedComment.getTitle())
            && Objects.equals(expectedComment, aUpdatedComment.getComment())
            && Objects.equals(MBTI.ENTJ, aUpdatedComment.getMbti())
            && Objects.equals(Enneagram.E_3W4, aUpdatedComment.getEnneagram())
            && Objects.equals(Zodiac.Gemini, aUpdatedComment.getZodiac())
            && Objects.equals(1, aUpdatedComment.getLike())
            && Objects.equals(aComment.getCreatedAt(), aUpdatedComment.getCreatedAt())
            && aComment.getUpdatedAt().isBefore(aUpdatedComment.getUpdatedAt())
    ));
  }

  @Test
  public void givenAValidCommand_whenCallsUpdateCommentWithoutLike_shouldReturnItsIdentifier() {
    // given
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    final var expectedId = aComment.getId();

    final var aCommand = UpdateCommentCommand.with(
        expectedId.getValue(),
        MBTI.ENTJ,
        Enneagram.E_3W4,
        Zodiac.Gemini
    );

    when(commentGateway.findById(any()))
        .thenReturn(Optional.of(Comment.with(aComment)));

    when(commentGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualOutput = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    verify(commentGateway).findById(eq(expectedId));

    verify(commentGateway).update(argThat(aUpdatedComment ->
        Objects.equals(expectedId, aUpdatedComment.getId())
            && Objects.equals(expectedTitle, aUpdatedComment.getTitle())
            && Objects.equals(expectedComment, aUpdatedComment.getComment())
            && Objects.equals(MBTI.ENTJ, aUpdatedComment.getMbti())
            && Objects.equals(Enneagram.E_3W4, aUpdatedComment.getEnneagram())
            && Objects.equals(Zodiac.Gemini, aUpdatedComment.getZodiac())
            && Objects.equals(aComment.getCreatedAt(), aUpdatedComment.getCreatedAt())
            && aComment.getUpdatedAt().isBefore(aUpdatedComment.getUpdatedAt())
    ));
  }

  @Test
  public void givenAValidCommand_whenCallsUpdateCommentJustLike_shouldReturnItsIdentifier() {
    // given
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    final var expectedId = aComment.getId();

    final var aCommand = UpdateCommentCommand.with(
        expectedId.getValue(),
        1
    );

    when(commentGateway.findById(any()))
        .thenReturn(Optional.of(Comment.with(aComment)));

    when(commentGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualOutput = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    verify(commentGateway).findById(eq(expectedId));

    verify(commentGateway).update(argThat(aUpdatedComment ->
        Objects.equals(expectedId, aUpdatedComment.getId())
            && Objects.equals(expectedTitle, aUpdatedComment.getTitle())
            && Objects.equals(expectedComment, aUpdatedComment.getComment())
            && Objects.equals(1, aUpdatedComment.getLike())
            && Objects.equals(aComment.getCreatedAt(), aUpdatedComment.getCreatedAt())
            && aComment.getUpdatedAt().isBefore(aUpdatedComment.getUpdatedAt())
    ));
  }

  @Test
  public void givenAInvalidId_whenCallsUpdateComment_shouldThrowsNotFoundException() {
    // given
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    final var expectedId = CommentID.from("123");

    final var expectedErrorMessage = "Comment with ID 123 was not found";

    final var aCommand = UpdateCommentCommand.with(
        expectedId.getValue(),
        MBTI.ENTJ,
        Enneagram.E_3W4,
        Zodiac.Gemini,
        1
    );

    when(commentGateway.findById(any()))
        .thenReturn(Optional.empty());

    // when
    final var actualException = Assertions.assertThrows(NotFoundException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    verify(commentGateway).findById(eq(expectedId));
    verify(commentGateway, times(0)).update(any());
  }
}
