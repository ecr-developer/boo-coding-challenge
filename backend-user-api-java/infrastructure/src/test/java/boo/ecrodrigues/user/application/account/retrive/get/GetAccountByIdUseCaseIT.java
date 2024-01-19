package boo.ecrodrigues.user.application.account.retrive.get;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

import boo.ecrodrigues.user.IntegrationTest;
import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.exceptions.NotFoundException;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountEntity;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountRepository;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class GetAccountByIdUseCaseIT {

  @Autowired
  private GetAccountByIdUseCase useCase;

  @Autowired
  private AccountRepository accountRepository;

  @SpyBean
  private AccountGateway accountGateway;

  @Test
  public void givenAValidId_whenCallsGetAccount_shouldReturnAccount() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    final var anAccount =
        Account.newAccount(expectedName, expectedIsActive);

    final var expectedId = anAccount.getId();

    save(anAccount);

    final var actualAccount = useCase.execute(expectedId.getValue());

    Assertions.assertEquals(expectedId, actualAccount.id());
    Assertions.assertEquals(expectedName, actualAccount.name());
    Assertions.assertEquals(expectedIsActive, actualAccount.isActive());
    Assertions.assertEquals(anAccount.getCreatedAt(), actualAccount.createdAt());
    Assertions.assertEquals(anAccount.getUpdatedAt(), actualAccount.updatedAt());
    Assertions.assertEquals(anAccount.getDeletedAt(), actualAccount.deletedAt());
  }

  @Test
  public void givenAInvalidId_whenCallsGetAccount_shouldReturnNotFound() {
    final var expectedErrorMessage = "Account with ID 123 was not found";
    final var expectedId = AccountID.from("123");

    final var actualException = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(expectedId.getValue())
    );

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
  }

  @Test
  public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
    final var expectedErrorMessage = "Gateway error";
    final var expectedId = AccountID.from("123");

    doThrow(new IllegalStateException(expectedErrorMessage))
        .when(accountGateway).findById(eq(expectedId));

    final var actualException = Assertions.assertThrows(
        IllegalStateException.class,
        () -> useCase.execute(expectedId.getValue())
    );

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
  }

  private void save(final Account... anAccount) {
    accountRepository.saveAll(
        Arrays.stream(anAccount)
            .map(AccountEntity::from)
            .toList()
    );
  }
}
