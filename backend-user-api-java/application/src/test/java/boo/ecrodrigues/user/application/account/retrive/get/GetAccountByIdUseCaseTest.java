package boo.ecrodrigues.user.application.account.retrive.get;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import boo.ecrodrigues.user.application.UseCaseTest;
import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.exceptions.NotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class GetAccountByIdUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DefaultGetAccountByIdUseCase useCase;

  @Mock
  private AccountGateway accountGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(accountGateway);
  }

  @Test
  public void givenAValidId_whenCallsGetAccount_shouldReturnAccount() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    final var anAccount =
        Account.newAccount(expectedName, expectedIsActive);

    final var expectedId = anAccount.getId();

    when(accountGateway.findById(eq(expectedId)))
        .thenReturn(Optional.of(anAccount.clone()));

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

    when(accountGateway.findById(eq(expectedId)))
        .thenReturn(Optional.empty());

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

    when(accountGateway.findById(eq(expectedId)))
        .thenThrow(new IllegalStateException(expectedErrorMessage));

    final var actualException = Assertions.assertThrows(
        IllegalStateException.class,
        () -> useCase.execute(expectedId.getValue())
    );

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
  }
}
