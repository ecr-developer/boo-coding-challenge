package boo.ecrodrigues.user.application.account.update;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

import boo.ecrodrigues.user.IntegrationTest;
import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.domain.exceptions.NotFoundException;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountEntity;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountRepository;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class UpdateAccountUseCaseIT {

  @Autowired
  private UpdateAccountUseCase useCase;

  @Autowired
  private AccountRepository accountRepository;

  @SpyBean
  private AccountGateway accountGateway;

  @Test
  public void givenAValidCommand_whenCallsUpdateAccount_shouldReturnAccountId() {
    final var anAccount = Account.newAccount("A Martinez", true);

    save(anAccount);

    final var expectedName = "Andres Martinez";
    final var expectedIsActive = true;
    final var expectedId = anAccount.getId();

    final var aCommand = UpdateAccountCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedIsActive
    );

    Assertions.assertEquals(1, accountRepository.count());

    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    final var actualAccount =
        accountRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, actualAccount.getName());
    Assertions.assertEquals(expectedIsActive, actualAccount.isActive());
    Assertions.assertEquals(anAccount.getCreatedAt(), actualAccount.getCreatedAt());
    Assertions.assertTrue(anAccount.getUpdatedAt().isBefore(actualAccount.getUpdatedAt()));
    Assertions.assertNull(actualAccount.getDeletedAt());
  }

  @Test
  public void givenAInvalidName_whenCallsUpdateAccount_thenShouldReturnDomainException() {
    final var anAccount = Account.newAccount("A Martinez", true);

    save(anAccount);

    final String expectedName = null;
    final var expectedIsActive = true;
    final var expectedId = anAccount.getId();

    final var expectedErrorMessage = "'name' should not be null";
    final var expectedErrorCount = 1;

    final var aCommand =
        UpdateAccountCommand.with(expectedId.getValue(), expectedName, expectedIsActive);

    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

    Mockito.verify(accountGateway, times(0)).update(any());
  }

  @Test
  public void givenAValidInactivateCommand_whenCallsUpdateAccount_shouldReturnInactiveAccountId() {
    final var anAccount = Account.newAccount("A Martinez", true);

    save(anAccount);

    final var expectedName = "Andres Martinez";
    final var expectedIsActive = false;
    final var expectedId = anAccount.getId();

    final var aCommand = UpdateAccountCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedIsActive
    );

    Assertions.assertTrue(anAccount.isActive());
    Assertions.assertNull(anAccount.getDeletedAt());

    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    final var actualAccount =
        accountRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, actualAccount.getName());
    Assertions.assertEquals(expectedIsActive, actualAccount.isActive());
    Assertions.assertEquals(anAccount.getCreatedAt(), actualAccount.getCreatedAt());
    Assertions.assertTrue(anAccount.getUpdatedAt().isBefore(actualAccount.getUpdatedAt()));
    Assertions.assertNotNull(actualAccount.getDeletedAt());
  }

  @Test
  public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
    final var anAccount = Account.newAccount("A Martinez", true);

    save(anAccount);

    final var expectedName = "Andres Martinez";
    final var expectedIsActive = true;
    final var expectedId = anAccount.getId();
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "Gateway error";

    final var aCommand = UpdateAccountCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedIsActive
    );

    doThrow(new IllegalStateException(expectedErrorMessage))
        .when(accountGateway).update(any());

    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

    final var actualAccount =
        accountRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(anAccount.getName(), actualAccount.getName());
    Assertions.assertEquals(anAccount.isActive(), actualAccount.isActive());
    Assertions.assertEquals(anAccount.getCreatedAt(), actualAccount.getCreatedAt());
    Assertions.assertEquals(anAccount.getUpdatedAt(), actualAccount.getUpdatedAt());
    Assertions.assertEquals(anAccount.getDeletedAt(), actualAccount.getDeletedAt());
  }

  @Test
  public void givenACommandWithInvalidID_whenCallsUpdateAccount_shouldReturnNotFoundException() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = false;
    final var expectedId = "123";
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "Account with ID 123 was not found";

    final var aCommand = UpdateAccountCommand.with(
        expectedId,
        expectedName,
        expectedIsActive
    );

    final var actualException =
        Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

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
