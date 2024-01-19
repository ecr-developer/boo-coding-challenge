package boo.ecrodrigues.user.application.account.create;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

import boo.ecrodrigues.user.IntegrationTest;
import boo.ecrodrigues.user.application.account.create.CreateAccountCommand;
import boo.ecrodrigues.user.application.account.create.CreateAccountUseCase;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class CreateAccountUseCaseIT {

  @Autowired
  private CreateAccountUseCase useCase;

  @Autowired
  private AccountRepository accountRepository;

  @SpyBean
  private AccountGateway accountGateway;

  @Test
  public void givenAValidCommand_whenCallsCreateAccount_shouldReturnAccountId() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    Assertions.assertEquals(0, accountRepository.count());

    final var aCommand =
        CreateAccountCommand.with(expectedName, expectedIsActive);

    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    Assertions.assertEquals(1, accountRepository.count());

    final var actualAccount =
        accountRepository.findById(actualOutput.id()).get();

    Assertions.assertEquals(expectedName, actualAccount.getName());
    Assertions.assertEquals(expectedIsActive, actualAccount.isActive());
    Assertions.assertNotNull(actualAccount.getCreatedAt());
    Assertions.assertNotNull(actualAccount.getUpdatedAt());
    Assertions.assertNull(actualAccount.getDeletedAt());
  }

  @Test
  public void givenAInvalidName_whenCallsCreateAccount_thenShouldReturnDomainException() {
    final String expectedName = null;
    final var expectedIsActive = true;
    final var expectedErrorMessage = "'name' should not be null";
    final var expectedErrorCount = 1;

    Assertions.assertEquals(0, accountRepository.count());

    final var aCommand =
        CreateAccountCommand.with(expectedName, expectedIsActive);

    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

    Assertions.assertEquals(0, accountRepository.count());

    Mockito.verify(accountGateway, times(0)).create(any());
  }

  @Test
  public void givenAValidCommandWithInactiveAccount_whenCallsCreateAccount_shouldReturnInactiveAccountId() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = false;

    Assertions.assertEquals(0, accountRepository.count());

    final var aCommand =
        CreateAccountCommand.with(expectedName, expectedIsActive);

    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    Assertions.assertEquals(1, accountRepository.count());

    final var actualAccount =
        accountRepository.findById(actualOutput.id()).get();

    Assertions.assertEquals(expectedName, actualAccount.getName());
    Assertions.assertEquals(expectedIsActive, actualAccount.isActive());
    Assertions.assertNotNull(actualAccount.getCreatedAt());
    Assertions.assertNotNull(actualAccount.getUpdatedAt());
    Assertions.assertNotNull(actualAccount.getDeletedAt());
  }

  @Test
  public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "Gateway error";

    final var aCommand =
        CreateAccountCommand.with(expectedName, expectedIsActive);

    doThrow(new IllegalStateException(expectedErrorMessage))
        .when(accountGateway).create(any());

    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
  }
}
