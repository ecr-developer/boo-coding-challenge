package boo.ecrodrigues.user.application.account.retrive.list;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import boo.ecrodrigues.user.application.UseCaseTest;
import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ListAccountsUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DefaultListAccountsUseCase useCase;

  @Mock
  private AccountGateway accountGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(accountGateway);
  }

  @Test
  public void givenAValidQuery_whenCallsListAccounts_thenShouldReturnAccounts() {
    final var categories = List.of(
        Account.newAccount("A Martinez", true),
        Account.newAccount("G Fernandez", true)
    );

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    final var expectedPagination =
        new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

    final var expectedItemsCount = 2;
    final var expectedResult = expectedPagination.map(AccountListOutput::from);

    when(accountGateway.findAll(eq(aQuery)))
        .thenReturn(expectedPagination);

    final var actualResult = useCase.execute(aQuery);

    Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
    Assertions.assertEquals(expectedResult, actualResult);
    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(categories.size(), actualResult.total());
  }

  @Test
  public void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyAccounts() {
    final var accounts = List.<Account>of();

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    final var expectedPagination =
        new Pagination<>(expectedPage, expectedPerPage, accounts.size(), accounts);

    final var expectedItemsCount = 0;
    final var expectedResult = expectedPagination.map(AccountListOutput::from);

    when(accountGateway.findAll(eq(aQuery)))
        .thenReturn(expectedPagination);

    final var actualResult = useCase.execute(aQuery);

    Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
    Assertions.assertEquals(expectedResult, actualResult);
    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(accounts.size(), actualResult.total());
  }

  @Test
  public void givenAValidQuery_whenGatewayThrowsException_shouldReturnException() {
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedErrorMessage = "Gateway error";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    when(accountGateway.findAll(eq(aQuery)))
        .thenThrow(new IllegalStateException(expectedErrorMessage));

    final var actualException =
        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
  }
}
