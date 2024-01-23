package boo.ecrodrigues.user.application.comment.update;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import boo.ecrodrigues.user.IntegrationTest;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.comment.Comment;
import boo.ecrodrigues.user.domain.comment.CommentGateway;
import boo.ecrodrigues.user.domain.comment.CommentID;
import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import boo.ecrodrigues.user.domain.exceptions.NotFoundException;
import boo.ecrodrigues.user.infrastructure.comment.persistence.CommentEntity;
import boo.ecrodrigues.user.infrastructure.comment.persistence.CommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class UpdateCommentUseCaseIT {

  @Autowired
  private UpdateCommentUseCase useCase;

  @Autowired
  private CommentRepository commentRepository;

  @SpyBean
  private CommentGateway commentGateway;

  @Test
  public void givenAValidCommand_whenCallsUpdateComment_shouldReturnItsIdentifier() {
    // given
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    this.commentRepository.save(CommentEntity.from(aComment));

    final var expectedId = aComment.getId();

    final var aCommand = UpdateCommentCommand.with(
        expectedId.getValue(),
        MBTI.ENTJ,
        Enneagram.E_3W4,
        Zodiac.Gemini
    );

    // when
    final var actualOutput = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    final var actualPersistedMember = this.commentGateway.findById(expectedId).get();

    Assertions.assertEquals(expectedTitle, actualPersistedMember.getTitle());
    Assertions.assertEquals(expectedComment, actualPersistedMember.getComment());
    Assertions.assertEquals(MBTI.ENTJ, actualPersistedMember.getMbti());
    Assertions.assertEquals(Enneagram.E_3W4, actualPersistedMember.getEnneagram());
    Assertions.assertEquals(Zodiac.Gemini, actualPersistedMember.getZodiac());
    Assertions.assertEquals(aComment.getCreatedAt(), actualPersistedMember.getCreatedAt());
    Assertions.assertTrue(aComment.getUpdatedAt().isBefore(actualPersistedMember.getUpdatedAt()));
  }

  @Test
  public void givenAnInvalidId_whenCallsUpdateComment_shouldThrowsNotFoundException() {
    // given
    final var expectedId = CommentID.from("123");

    final var expectedErrorMessage = "Comment with ID 123 was not found";

    final var aCommand = UpdateCommentCommand.with(
        expectedId.getValue(),
        MBTI.ENTJ,
        Enneagram.E_3W4,
        Zodiac.Gemini
    );

    // when
    final var actualException = Assertions.assertThrows(NotFoundException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    verify(commentGateway).findById(any());
    verify(commentGateway, times(0)).update(any());
  }
}
