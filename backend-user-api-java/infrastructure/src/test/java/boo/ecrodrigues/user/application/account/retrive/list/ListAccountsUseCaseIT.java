package boo.ecrodrigues.user.application.account.retrive.list;

import boo.ecrodrigues.user.IntegrationTest;
import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountEntity;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountRepository;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class ListAccountsUseCaseIT {

  @Autowired
  private ListAccountsUseCase useCase;

  @Autowired
  private AccountRepository accountRepository;

  @BeforeEach
  void mockUp() {
    final var accounts = Stream.of(
            Account.newAccount("A Martinez", true),
            Account.newAccount("G Martinez", true),
            Account.newAccount("D Fernandez", true),
            Account.newAccount("E Rodrigues", true),
            Account.newAccount("P Rodrigues", true),
            Account.newAccount("F Santos", true),
            Account.newAccount("B Castro", true)
        )
        .map(AccountEntity::from)
        .toList();

    accountRepository.saveAll(accounts);
  }

  @Test
  public void givenAValidTerm_whenTermDoesntMatchsPrePersisted_shouldReturnEmptyPage() {
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "ji1j3i 1j3i1oj";
    final var expectedSort = "name";
    final var expectedDirection = "asc";
    final var expectedItemsCount = 0;
    final var expectedTotal = 0;

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    final var actualResult = useCase.execute(aQuery);

    Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
  }

  @ParameterizedTest
  @CsvSource({
      "A,0,10,1,1,A Martinez",
      "G,0,10,1,1,G Martinez",
      "E,0,10,1,1,E Rodrigues",
  })
  public void givenAValidTerm_whenCallsListAccounts_shouldReturnAccountsFiltered(
      final String expectedTerms,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedAccountName
  ) {
    final var expectedSort = "name";
    final var expectedDirection = "asc";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    final var actualResult = useCase.execute(aQuery);

    Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
    Assertions.assertEquals(expectedAccountName, actualResult.items().get(0).name());
  }

  @ParameterizedTest
  @CsvSource({
      "name,asc,0,10,7,7,A Martinez",
      "name,desc,0,10,7,7,P Rodrigues",
      "createdAt,asc,0,10,7,7,A Martinez",
      "createdAt,desc,0,10,7,7,A Martinez",
  })
  public void givenAValidSortAndDirection_whenCallsListAccounts_thenShouldReturnAccountsOrdered(
      final String expectedSort,
      final String expectedDirection,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedCategoryName
  ) {
    final var expectedTerms = "";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    final var actualResult = useCase.execute(aQuery);

    Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
    Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
  }

  @ParameterizedTest
  @CsvSource({
      "0,2,2,7,A Martinez",
      "1,2,2,7,D Fernandez",
      "2,2,2,7,F Santos",
      "3,2,1,7,P Rodrigues",
  })
  public void givenAValidPage_whenCallsListAccounts_shouldReturnAccountsPaginated(
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedAccountsName
  ) {
    final var expectedSort = "name";
    final var expectedDirection = "asc";
    final var expectedTerms = "";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    final var actualResult = useCase.execute(aQuery);

    Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());

    int index = 0;
    for (final String expectedName : expectedAccountsName.split(";")) {
      final String actualName = actualResult.items().get(index).name();
      Assertions.assertEquals(expectedName, actualName);
      index++;
    }
  }
}
