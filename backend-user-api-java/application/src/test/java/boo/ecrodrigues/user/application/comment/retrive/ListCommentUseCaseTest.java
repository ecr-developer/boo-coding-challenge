package boo.ecrodrigues.user.application.comment.retrive;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import boo.ecrodrigues.user.application.UseCaseTest;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.comment.Comment;
import boo.ecrodrigues.user.domain.comment.CommentGateway;
import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ListCommentUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DefaultListCommentsUseCase useCase;

  @Mock
  private CommentGateway commentGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(commentGateway);
  }

  @Test
  public void givenAValidQuery_whenCallsListComments_shouldReturnAll() {
    // given
    final var comments = List.of(
        Comment.newComment(AccountID.unique(), "Title Celebrety test", "Comment Celebrety test ..."),
        Comment.newComment(AccountID.unique(), "Title Celebrety test 2", "Comment Celebrety test 2 ...")
    );

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "2";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 2;

    final var expectedItems = comments.stream()
        .map(CommentListOutput::from)
        .toList();

    final var expectedPagination = new Pagination<>(
        expectedPage,
        expectedPerPage,
        expectedTotal,
        comments
    );

    when(commentGateway.findAll(any()))
        .thenReturn(expectedPagination);

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualOutput = useCase.execute(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());
    Assertions.assertEquals(expectedItems, actualOutput.items());

    verify(commentGateway).findAll(eq(aQuery));
  }

  @Test
  public void givenAValidQuery_whenCallsListCommentsAndIsEmpty_shouldReturn() {
    // given
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "Something";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 0;

    final var comments = List.<Comment>of();
    final var expectedItems = List.<CommentListOutput>of();

    final var expectedPagination = new Pagination<>(
        expectedPage,
        expectedPerPage,
        expectedTotal,
        comments
    );

    when(commentGateway.findAll(any()))
        .thenReturn(expectedPagination);

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualOutput = useCase.execute(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());
    Assertions.assertEquals(expectedItems, actualOutput.items());

    verify(commentGateway).findAll(eq(aQuery));
  }

  @Test
  public void givenAValidQuery_whenCallsListCommentsAndGatewayThrowsRandomException_shouldException() {
    // given
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "Something";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var expectedErrorMessage = "Gateway error";

    when(commentGateway.findAll(any()))
        .thenThrow(new IllegalStateException(expectedErrorMessage));

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> {
      useCase.execute(aQuery);
    });

    // then
    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    verify(commentGateway).findAll(eq(aQuery));
  }
}
