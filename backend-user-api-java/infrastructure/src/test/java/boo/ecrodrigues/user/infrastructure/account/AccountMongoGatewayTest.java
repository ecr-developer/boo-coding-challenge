package boo.ecrodrigues.user.infrastructure.account;

import boo.ecrodrigues.user.IntegrationTest;
import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountEntity;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountRepository;
import boo.ecrodrigues.user.infrastructure.configuration.DatabaseCollectionsConfig;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

@IntegrationTest
public class AccountMongoGatewayTest {

  private static final String COLLECTION_ACCOUNT_NAME = "Accounts";

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private DatabaseCollectionsConfig databaseCollectionsConfig;

  @Autowired
  private AccountRepository accountRepository;

  private AccountMongoGateway accountGateway;


  @BeforeEach
  public void setup() {
    accountGateway = new AccountMongoGateway(mongoTemplate, databaseCollectionsConfig, accountRepository);
  }

  @Test
  public void givenAValidAccount_whenCallsCreate_shouldReturnANewAccount() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    final var anAccount = Account.newAccount(expectedName, expectedIsActive);

    Assertions.assertEquals(0, accountRepository.count());

    final var actualAccount = accountGateway.create(anAccount);

    Assertions.assertEquals(1, accountRepository.count());

    Assertions.assertEquals(anAccount.getId(), actualAccount.getId());
    Assertions.assertEquals(expectedName, actualAccount.getName());
    Assertions.assertEquals(expectedIsActive, actualAccount.isActive());
    Assertions.assertEquals(anAccount.getCreatedAt(), actualAccount.getCreatedAt());
    Assertions.assertEquals(anAccount.getUpdatedAt(), actualAccount.getUpdatedAt());
    Assertions.assertEquals(anAccount.getDeletedAt(), actualAccount.getDeletedAt());
    Assertions.assertNull(actualAccount.getDeletedAt());

    final var actualEntity = accountRepository.findById(anAccount.getId().getValue()).get();

    Assertions.assertEquals(anAccount.getId().getValue(), actualEntity.getId());
    Assertions.assertEquals(expectedName, actualEntity.getName());
    Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
    Assertions.assertEquals(anAccount.getCreatedAt(), actualEntity.getCreatedAt());
    Assertions.assertEquals(anAccount.getUpdatedAt(), actualEntity.getUpdatedAt());
    Assertions.assertEquals(anAccount.getDeletedAt(), actualEntity.getDeletedAt());
    Assertions.assertNull(actualEntity.getDeletedAt());
  }

  @Test
  public void givenAValidAccount_whenCallsUpdate_shouldReturnAccountUpdated() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    final var anAccount = Account.newAccount("Antunez Martinez", expectedIsActive);

    Assertions.assertEquals(0, accountRepository.count());

    accountRepository.save(AccountEntity.from(anAccount));

    Assertions.assertEquals(1, accountRepository.count());

    final var actualInvalidEntity = accountRepository.findById(anAccount.getId().getValue()).get();

    Assertions.assertEquals("Antunez Martinez", actualInvalidEntity.getName());
    Assertions.assertEquals(expectedIsActive, actualInvalidEntity.isActive());

    final var anUpdatedAccount = anAccount.clone()
        .update(expectedName, expectedIsActive);

    final var actualAccount = accountGateway.update(anUpdatedAccount);

    Assertions.assertEquals(1, accountRepository.count());

    Assertions.assertEquals(anAccount.getId(), actualAccount.getId());
    Assertions.assertEquals(expectedName, actualAccount.getName());
    Assertions.assertEquals(expectedIsActive, actualAccount.isActive());
    Assertions.assertEquals(anAccount.getCreatedAt(), actualAccount.getCreatedAt());
    Assertions.assertTrue(anAccount.getUpdatedAt().isBefore(actualAccount.getUpdatedAt()));
    Assertions.assertEquals(anAccount.getDeletedAt(), actualAccount.getDeletedAt());
    Assertions.assertNull(actualAccount.getDeletedAt());

    final var actualEntity = accountRepository.findById(anAccount.getId().getValue()).get();

    Assertions.assertEquals(anAccount.getId().getValue(), actualEntity.getId());
    Assertions.assertEquals(expectedName, actualEntity.getName());
    Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
    Assertions.assertEquals(anAccount.getCreatedAt(), actualEntity.getCreatedAt());
    Assertions.assertTrue(anAccount.getUpdatedAt().isBefore(actualAccount.getUpdatedAt()));
    Assertions.assertEquals(anAccount.getDeletedAt(), actualEntity.getDeletedAt());
    Assertions.assertNull(actualEntity.getDeletedAt());
  }

  @Test
  public void givenAPrePersistedAccountAndValidAccountId_whenTryToDeleteIt_shouldDeleteAccount() {
    final var anAccount = Account.newAccount("A Martinez", true);

    Assertions.assertEquals(0, accountRepository.count());

    accountRepository.save(AccountEntity.from(anAccount));

    Assertions.assertEquals(1, accountRepository.count());

    accountGateway.deleteById(anAccount.getId().getValue());

    Assertions.assertEquals(1, accountRepository.count());

    final var actualEntity = accountRepository.findById(anAccount.getId().getValue()).get();

    Assertions.assertEquals(anAccount.getId().getValue(), actualEntity.getId());
    Assertions.assertEquals(anAccount.getName(), actualEntity.getName());
    Assertions.assertEquals(false, actualEntity.isActive());
  }

  @Test
  public void givenAPrePersistedAccountAndValidAccountId_whenCallsFindById_shouldReturnAccount() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    final var anAccount = Account.newAccount(expectedName, expectedIsActive);

    Assertions.assertEquals(0, accountRepository.count());

    accountRepository.save(AccountEntity.from(anAccount));

    Assertions.assertEquals(1, accountRepository.count());

    final var actualAccount = accountGateway.findById(anAccount.getId()).get();

    Assertions.assertEquals(1, accountRepository.count());

    Assertions.assertEquals(anAccount.getId(), actualAccount.getId());
    Assertions.assertEquals(expectedName, actualAccount.getName());
    Assertions.assertEquals(expectedIsActive, actualAccount.isActive());
    Assertions.assertEquals(anAccount.getCreatedAt(), actualAccount.getCreatedAt());
    Assertions.assertEquals(anAccount.getUpdatedAt(), actualAccount.getUpdatedAt());
    Assertions.assertEquals(anAccount.getDeletedAt(), actualAccount.getDeletedAt());
    Assertions.assertNull(actualAccount.getDeletedAt());
  }

  @Test
  public void givenValidAccountIdNotStored_whenCallsFindById_shouldReturnEmpty() {
    Assertions.assertEquals(0, accountRepository.count());

    final var actualAccount = accountGateway.findById(AccountID.from("empty"));

    Assertions.assertTrue(actualAccount.isEmpty());
  }

  @Test
  public void givenPrePersistedAccounts_whenCallsFindAll_shouldReturnPaginated() {
    final var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTotal = 3;

    final var anAccount = Account.newAccount("A Martinez", true);
    final var aSecoundAccount = Account.newAccount("P Rodrigues", true);
    final var aThirdAccount = Account.newAccount("F Santos", true);

    Assertions.assertEquals(0, accountRepository.count());

    accountRepository.saveAll(List.of(
        AccountEntity.from(anAccount),
        AccountEntity.from(aSecoundAccount),
        AccountEntity.from(aThirdAccount)
    ));

    Assertions.assertEquals(3, accountRepository.count());

    final var query = new SearchQuery(0, 1, "", "name", "asc");
    final var actualResult = accountGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
    Assertions.assertEquals(expectedPerPage, actualResult.items().size());
    Assertions.assertEquals(anAccount.getId(), actualResult.items().get(0).getId());
  }

  @Test
  public void givenEmptyAccountsTable_whenCallsFindAll_shouldReturnEmptyPage() {
    final var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTotal = 0;

    Assertions.assertEquals(0, accountRepository.count());

    final var query = new SearchQuery(0, 1, "", "name", "asc");
    final var actualResult = accountGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
    Assertions.assertEquals(0, actualResult.items().size());
  }

  @Test
  public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated() {
    var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTotal = 3;

    final var anAccount = Account.newAccount("A Martinez", true);
    final var aSecoundAccount = Account.newAccount("P Rodrigues", true);
    final var aThirdAccount = Account.newAccount("F Santos", true);

    Assertions.assertEquals(0, accountRepository.count());

    accountRepository.saveAll(List.of(
        AccountEntity.from(anAccount),
        AccountEntity.from(aSecoundAccount),
        AccountEntity.from(aThirdAccount)
    ));

    Assertions.assertEquals(3, accountRepository.count());

    var query = new SearchQuery(0, 1, "", "name", "asc");
    var actualResult = accountGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
    Assertions.assertEquals(expectedPerPage, actualResult.items().size());
    Assertions.assertEquals(anAccount.getId(), actualResult.items().get(0).getId());

    // Page 1
    expectedPage = 1;

    query = new SearchQuery(1, 1, "", "name", "asc");
    actualResult = accountGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
    Assertions.assertEquals(expectedPerPage, actualResult.items().size());
    Assertions.assertEquals(aThirdAccount.getId(), actualResult.items().get(0).getId());

    // Page 2
    expectedPage = 2;

    query = new SearchQuery(2, 1, "", "name", "asc");
    actualResult = accountGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
    Assertions.assertEquals(expectedPerPage, actualResult.items().size());
    Assertions.assertEquals(aSecoundAccount.getId(), actualResult.items().get(0).getId());
  }

  @Test
  public void givenPrePersistedAccountsAndDocAsTerms_whenCallsFindAllAndTermsMatchsAccountName_shouldReturnPaginated() {
    final var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTotal = 1;

    final var anAccount = Account.newAccount("A Martinez", true);
    final var aSecoundAccount = Account.newAccount("P Rodrigues", true);
    final var aThirdAccount = Account.newAccount("F Santos", true);

    Assertions.assertEquals(0, accountRepository.count());

    accountRepository.saveAll(List.of(
        AccountEntity.from(anAccount),
        AccountEntity.from(aSecoundAccount),
        AccountEntity.from(aThirdAccount)
    ));

    Assertions.assertEquals(3, accountRepository.count());

    final var query = new SearchQuery(0, 1, "F", "name", "asc");
    final var actualResult = accountGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
    Assertions.assertEquals(expectedPerPage, actualResult.items().size());
    Assertions.assertEquals(aThirdAccount.getId(), actualResult.items().get(0).getId());
  }
}
