package boo.ecrodrigues.user.application.account.create;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import boo.ecrodrigues.user.application.UseCaseTest;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class CreateAccountUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DefaultCreateAccountUseCase useCase;

  @Mock
  private AccountGateway accountGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(accountGateway);
  }

  @Test
  public void givenAValidCommand_whenCallsCreateAccount_shouldReturnAccountId() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    final var aCommand =
        CreateAccountCommand.with(expectedName, expectedIsActive);

    when(accountGateway.create(any()))
        .thenAnswer(returnsFirstArg());

    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    Mockito.verify(accountGateway, times(1)).create(argThat(anAccount ->
        Objects.equals(expectedName, anAccount.getName())
            && Objects.equals(expectedIsActive, anAccount.isActive())
            && Objects.nonNull(anAccount.getId())
            && Objects.nonNull(anAccount.getCreatedAt())
            && Objects.nonNull(anAccount.getUpdatedAt())
            && Objects.isNull(anAccount.getDeletedAt())
    ));
  }

  @Test
  public void givenAInvalidName_whenCallsCreateAccount_thenShouldReturnDomainException() {
    final String expectedName = null;
    final var expectedIsActive = true;
    final var expectedErrorMessage = "'name' should not be null";
    final var expectedErrorCount = 1;

    final var aCommand =
        CreateAccountCommand.with(expectedName, expectedIsActive);

    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

    Mockito.verify(accountGateway, times(0)).create(any());
  }

  @Test
  public void givenAValidCommandWithInactiveAccount_whenCallsCreateAccount_shouldReturnInactiveAccountId() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = false;

    final var aCommand =
        CreateAccountCommand.with(expectedName, expectedIsActive);

    when(accountGateway.create(any()))
        .thenAnswer(returnsFirstArg());

    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    Mockito.verify(accountGateway, times(1)).create(argThat(anAccount ->
        Objects.equals(expectedName, anAccount.getName())
            && Objects.equals(expectedIsActive, anAccount.isActive())
            && Objects.nonNull(anAccount.getId())
            && Objects.nonNull(anAccount.getCreatedAt())
            && Objects.nonNull(anAccount.getUpdatedAt())
            && Objects.nonNull(anAccount.getDeletedAt())
    ));
  }

  @Test
  public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "Gateway error";

    final var aCommand =
        CreateAccountCommand.with(expectedName, expectedIsActive);

    when(accountGateway.create(any()))
        .thenThrow(new IllegalStateException(expectedErrorMessage));

    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

    Mockito.verify(accountGateway, times(1)).create(argThat(anAccount ->
        Objects.equals(expectedName, anAccount.getName())
            && Objects.equals(expectedIsActive, anAccount.isActive())
            && Objects.nonNull(anAccount.getId())
            && Objects.nonNull(anAccount.getCreatedAt())
            && Objects.nonNull(anAccount.getUpdatedAt())
            && Objects.isNull(anAccount.getDeletedAt())
    ));
  }
}
