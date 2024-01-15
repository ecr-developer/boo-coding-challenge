package boo.ecrodrigues.user.infrastructure.account;

import boo.ecrodrigues.user.MongoGatewayTest;
import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountJpaEntity;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MongoGatewayTest
public class AccountMongoGatewayTest {

  @Autowired
  private AccountMongoGateway accountGateway;

  @Autowired
  private AccountRepository accountRepository;

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

    final var anAccount = Account.newAccount("G Martinez", expectedIsActive);

    Assertions.assertEquals(0, accountRepository.count());

    accountRepository.saveAndFlush(AccountJpaEntity.from(anAccount));

    Assertions.assertEquals(1, accountRepository.count());

    final var actualInvalidEntity = accountRepository.findById(anAccount.getId().getValue()).get();

    Assertions.assertEquals("G Martinez", actualInvalidEntity.getName());
    Assertions.assertEquals(expectedIsActive, actualInvalidEntity.isActive());

    final var aUpdatedAccount = anAccount.clone()
        .update(expectedName, expectedIsActive);

    final var actualAccount = accountGateway.update(aUpdatedAccount);

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
    final var anAccount = Account.newAccount("G Martinez", true);

    Assertions.assertEquals(0, accountRepository.count());

    accountRepository.saveAndFlush(AccountJpaEntity.from(anAccount));

    Assertions.assertEquals(1, accountRepository.count());

    accountGateway.deleteById(anAccount.getId());

    Assertions.assertEquals(0, accountRepository.count());
  }

  @Test
  public void givenInvalidAccountId_whenTryToDeleteIt_shouldDeleteAccount() {
    Assertions.assertEquals(0, accountRepository.count());

    accountGateway.deleteById(AccountID.from("invalid"));

    Assertions.assertEquals(0, accountRepository.count());
  }

  @Test
  public void givenAPrePersistedAccountAndValidAccountId_whenCallsFindById_shouldReturnAccount() {
    final var expectedName = "G Martinez";
    final var expectedIsActive = true;

    final var anAccount = Account.newAccount(expectedName, expectedIsActive);

    Assertions.assertEquals(0, accountRepository.count());

    accountRepository.saveAndFlush(AccountJpaEntity.from(anAccount));

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

}
