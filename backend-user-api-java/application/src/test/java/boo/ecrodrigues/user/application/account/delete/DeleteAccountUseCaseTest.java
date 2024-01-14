package boo.ecrodrigues.user.application.account.delete;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

import boo.ecrodrigues.user.application.UseCaseTest;
import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.domain.account.AccountID;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class DeleteAccountUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DeleteAccountUseCase useCase;

  @Mock
  private AccountGateway accountGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(accountGateway);
  }

  @Test
  public void givenAValidId_whenCallsDeleteAccount_shouldBeOK() {
    final var anAccount = Account.newAccount("A Martinez", true);
    final var expectedId = anAccount.getId();

    doNothing()
        .when(accountGateway).deleteById(eq(expectedId));

    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    Mockito.verify(accountGateway, times(1)).deleteById(eq(expectedId));
  }

  @Test
  public void givenAInvalidId_whenCallsDeleteAccount_shouldBeOK() {
    final var expectedId = AccountID.from("123");

    doNothing()
        .when(accountGateway).deleteById(eq(expectedId));

    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    Mockito.verify(accountGateway, times(1)).deleteById(eq(expectedId));
  }

  @Test
  public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
    final var anAccount = Account.newAccount("A Martinez", true);
    final var expectedId = anAccount.getId();

    doThrow(new IllegalStateException("Gateway error"))
        .when(accountGateway).deleteById(eq(expectedId));

    Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

    Mockito.verify(accountGateway, times(1)).deleteById(eq(expectedId));
  }
}
