package boo.ecrodrigues.user.application.comment.retrive;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import boo.ecrodrigues.user.IntegrationTest;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.comment.Comment;
import boo.ecrodrigues.user.domain.comment.CommentGateway;
import boo.ecrodrigues.user.domain.comment.CommentID;
import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import boo.ecrodrigues.user.domain.pagination.Fields;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;
import boo.ecrodrigues.user.domain.utils.InstantUtils;
import boo.ecrodrigues.user.infrastructure.comment.persistence.CommentEntity;
import boo.ecrodrigues.user.infrastructure.comment.persistence.CommentRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class ListCommentsUseCaseIT {

  @Autowired
  private ListCommentUseCase useCase;

  @Autowired
  private CommentRepository commentRepository;

  @SpyBean
  private CommentGateway commentGateway;

  @Test
  public void givenAValidQuery_whenCallsListComments_shouldReturnAll() {
    // given
    final var comments = List.of(
        CommentEntity.from(Comment.newComment(AccountID.unique(), "Title Celebrety test", "Comment Celebrety test ...")),
        CommentEntity.from(Comment.newComment(AccountID.unique(), "Title Celebrety test 2", "Comment Celebrety test 2 ..."))
    );

    this.commentRepository.saveAll(comments);

    Assertions.assertEquals(2, this.commentRepository.count());

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 2;

    final var expectedItems = comments.stream()
        .map(CommentEntity::toAggregate)
        .toList();

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    final var aFields = new Fields(null, null, null);

    // when
    final var actualOutput = useCase.execute(aQuery, aFields);

    // then
    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());

    verify(commentGateway).findAll(eq(aQuery), eq(aFields));
  }

  @Test
  public void givenAValidQueryWithMbtiFilter_whenCallsListComments_shouldReturnJustWithMbti() {
    // given
    final var comments = List.of(
        CommentEntity.from(Comment.newComment(AccountID.unique(), "Title Celebrety test", "Comment Celebrety test ...")),
        CommentEntity.from(
            Comment.with(
                CommentID.unique(),
                AccountID.unique(),
                "Title Celebrety test 2",
                "Comment Celebrety test 2 ...",
                MBTI.ENTJ,
                Enneagram.E_4W3,
                Zodiac.Gemini,
                1,
                InstantUtils.now(),
                InstantUtils.now()
            )
        )
    );

    this.commentRepository.saveAll(comments);

    Assertions.assertEquals(2, this.commentRepository.count());

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 1;

    final var expectedItems = comments.stream()
        .map(CommentEntity::toAggregate)
        .toList();

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    final var aFields = new Fields(MBTI.ENTJ.toString(), null, null);

    // when
    final var actualOutput = useCase.execute(aQuery, aFields);

    // then
    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());

    verify(commentGateway).findAll(eq(aQuery), eq(aFields));
  }

  @Test
  public void givenAValidQueryWithEnneagramFilter_whenCallsListComments_shouldReturnJustWithEnneagram() {
    // given
    final var comments = List.of(
        CommentEntity.from(Comment.newComment(AccountID.unique(), "Title Celebrety test", "Comment Celebrety test ...")),
        CommentEntity.from(
            Comment.with(
                CommentID.unique(),
                AccountID.unique(),
                "Title Celebrety test 2",
                "Comment Celebrety test 2 ...",
                MBTI.ENTJ,
                Enneagram.E_4W3,
                Zodiac.Gemini,
                1,
                InstantUtils.now(),
                InstantUtils.now()
            )
        )
    );

    this.commentRepository.saveAll(comments);

    Assertions.assertEquals(2, this.commentRepository.count());

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 1;

    final var expectedItems = comments.stream()
        .map(CommentEntity::toAggregate)
        .toList();

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    final var aFields = new Fields(null, Enneagram.E_4W3.toString(), null);

    // when
    final var actualOutput = useCase.execute(aQuery, aFields);

    // then
    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());

    verify(commentGateway).findAll(eq(aQuery), eq(aFields));
  }

  @Test
  public void givenAValidQueryWithZodiacFilter_whenCallsListComments_shouldReturnJustWithZodiac() {
    // given
    final var comments = List.of(
        CommentEntity.from(Comment.newComment(AccountID.unique(), "Title Celebrety test", "Comment Celebrety test ...")),
        CommentEntity.from(
            Comment.with(
                CommentID.unique(),
                AccountID.unique(),
                "Title Celebrety test 2",
                "Comment Celebrety test 2 ...",
                MBTI.ENTJ,
                Enneagram.E_4W3,
                Zodiac.Gemini,
                1,
                InstantUtils.now(),
                InstantUtils.now()
            )
        )
    );

    this.commentRepository.saveAll(comments);

    Assertions.assertEquals(2, this.commentRepository.count());

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 1;

    final var expectedItems = comments.stream()
        .map(CommentEntity::toAggregate)
        .toList();

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    final var aFields = new Fields(null, null, Zodiac.Gemini.toString());

    // when
    final var actualOutput = useCase.execute(aQuery, aFields);

    // then
    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());

    verify(commentGateway).findAll(eq(aQuery), eq(aFields));
  }

  @Test
  public void givenAValidQuery_whenCallsListCommentsAndIsEmpty_shouldReturn() {
    // given
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 0;

    final var expectedItems = List.<CommentListOutput>of();

    Assertions.assertEquals(0, this.commentRepository.count());

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    final var aFields = new Fields(null, null, null);

    // when
    final var actualOutput = useCase.execute(aQuery, aFields);

    // then
    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());
    Assertions.assertEquals(expectedItems, actualOutput.items());

    verify(commentGateway).findAll(eq(aQuery), eq(aFields));
  }

  @Test
  public void givenAValidQuery_whenCallsListCommentsAndGatewayThrowsRandomException_shouldException() {
    // given
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var expectedErrorMessage = "Gateway error";

    doThrow(new IllegalStateException(expectedErrorMessage))
        .when(commentGateway).findAll(any(), any());

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    final var aFields = new Fields(null, null, null);

    // when
    final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> {
      useCase.execute(aQuery, aFields);
    });

    // then
    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    verify(commentGateway).findAll(any(), any());
  }
}
