package boo.ecrodrigues.user.application.account.update;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import boo.ecrodrigues.user.application.UseCaseTest;
import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.exceptions.NotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class UpdateAccountUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DefaultUpdateAccountUseCase useCase;

  @Mock
  private AccountGateway accountGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(accountGateway);
  }

  @Test
  public void givenAValidCommand_whenCallsUpdateAccount_shouldReturnAccountId() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    final var anAccount =
        Account.newAccount(expectedName, expectedIsActive);

    final var expectedId = anAccount.getId();

    final var aCommand = UpdateAccountCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedIsActive
    );

    when(accountGateway.findById(eq(expectedId)))
        .thenReturn(Optional.of(Account.with(anAccount)));

    when(accountGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    Mockito.verify(accountGateway, times(1)).findById(eq(expectedId));

    Mockito.verify(accountGateway, times(1)).update(argThat(
        aUpdatedAccount ->
            Objects.equals(expectedName, aUpdatedAccount.getName())
                && Objects.equals(expectedIsActive, aUpdatedAccount.isActive())
                && Objects.equals(expectedId, aUpdatedAccount.getId())
                && Objects.equals(anAccount.getCreatedAt(), aUpdatedAccount.getCreatedAt())
                && anAccount.getUpdatedAt().isBefore(aUpdatedAccount.getUpdatedAt())
                && Objects.isNull(aUpdatedAccount.getDeletedAt())
    ));
  }

  @Test
  public void givenAInvalidName_whenCallsUpdateAccount_thenShouldReturnDomainException() {
    final var anAccount =
        Account.newAccount("A Martinez",true);

    final String expectedName = null;
    final var expectedIsActive = true;
    final var expectedId = anAccount.getId();

    final var expectedErrorMessage = "'name' should not be null";
    final var expectedErrorCount = 1;

    final var aCommand =
        UpdateAccountCommand.with(expectedId.getValue(), expectedName, expectedIsActive);

    when(accountGateway.findById(eq(expectedId)))
        .thenReturn(Optional.of(Account.with(anAccount)));

    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

    Mockito.verify(accountGateway, times(0)).update(any());
  }

  @Test
  public void givenAValidInactivateCommand_whenCallsUpdateAccount_shouldReturnInactiveAccountId() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = false;

    final var anAccount =
        Account.newAccount(expectedName,true);

    final var expectedId = anAccount.getId();

    final var aCommand = UpdateAccountCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedIsActive
    );

    when(accountGateway.findById(eq(expectedId)))
        .thenReturn(Optional.of(Account.with(anAccount)));

    when(accountGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    Assertions.assertTrue(anAccount.isActive());
    Assertions.assertNull(anAccount.getDeletedAt());

    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    Mockito.verify(accountGateway, times(1)).findById(eq(expectedId));

    Mockito.verify(accountGateway, times(1)).update(argThat(
        aUpdatedAccount ->
            Objects.equals(expectedName, aUpdatedAccount.getName())
                && Objects.equals(expectedIsActive, aUpdatedAccount.isActive())
                && Objects.equals(expectedId, aUpdatedAccount.getId())
                && Objects.equals(anAccount.getCreatedAt(), aUpdatedAccount.getCreatedAt())
                && anAccount.getUpdatedAt().isBefore(aUpdatedAccount.getUpdatedAt())
                && Objects.nonNull(aUpdatedAccount.getDeletedAt())
    ));
  }

  @Test
  public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
    final var anAccount =
        Account.newAccount("A Martinez", true);

    final var expectedName = "F Martinez";
    final var expectedIsActive = true;
    final var expectedId = anAccount.getId();
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "Gateway error";

    final var aCommand = UpdateAccountCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedIsActive
    );

    when(accountGateway.findById(eq(expectedId)))
        .thenReturn(Optional.of(Account.with(anAccount)));

    when(accountGateway.update(any()))
        .thenThrow(new IllegalStateException(expectedErrorMessage));

    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

    Mockito.verify(accountGateway, times(1)).update(argThat(
        aUpdatedAccount ->
            Objects.equals(expectedName, aUpdatedAccount.getName())
                && Objects.equals(expectedIsActive, aUpdatedAccount.isActive())
                && Objects.equals(expectedId, aUpdatedAccount.getId())
                && Objects.equals(anAccount.getCreatedAt(), aUpdatedAccount.getCreatedAt())
                && anAccount.getUpdatedAt().isBefore(aUpdatedAccount.getUpdatedAt())
                && Objects.isNull(aUpdatedAccount.getDeletedAt())
    ));
  }

  @Test
  public void givenACommandWithInvalidID_whenCallsUpdateAccount_shouldReturnNotFoundException() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = false;
    final var expectedId = "123";
    final var expectedErrorMessage = "Account with ID 123 was not found";

    final var aCommand = UpdateAccountCommand.with(
        expectedId,
        expectedName,
        expectedIsActive
    );

    when(accountGateway.findById(eq(AccountID.from(expectedId))))
        .thenReturn(Optional.empty());

    final var actualException =
        Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    Mockito.verify(accountGateway, times(1)).findById(eq(AccountID.from(expectedId)));

    Mockito.verify(accountGateway, times(0)).update(any());
  }
}
