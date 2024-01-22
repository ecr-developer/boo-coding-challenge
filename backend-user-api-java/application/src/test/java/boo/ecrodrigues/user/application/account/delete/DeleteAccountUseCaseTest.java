package boo.ecrodrigues.user.application.account.delete;

import boo.ecrodrigues.user.application.UseCaseTest;
import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
  public void givenAValidAccount_whenCallsDeleteAccount_shouldBeOK() {
    final var anAccount = Account.newAccount("A Martinez", true);

    doNothing()
        .when(accountGateway).deleteById(eq(anAccount.getId().getValue()));

    Assertions.assertDoesNotThrow(() -> useCase.execute(anAccount.getId().getValue()));

    Mockito.verify(accountGateway, times(1)).deleteById(eq(anAccount.getId().getValue()));
  }

  @Test
  public void givenAInvalidId_whenCallsDeleteAccount_shouldBeOK() {
    final var anAccount = Account.with(
        AccountID.from("123"),
        "A Martinez",
        false,
        InstantUtils.now(),
        InstantUtils.now(),
        InstantUtils.now()
    );


    doNothing()
        .when(accountGateway).deleteById(eq(anAccount.getId().getValue()));

    Assertions.assertDoesNotThrow(() -> useCase.execute(anAccount.getId().getValue()));

    Mockito.verify(accountGateway, times(1)).deleteById(eq(anAccount.getId().getValue()));
  }

  @Test
  public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
    final var anAccount = Account.newAccount("A Martinez", true);

    doThrow(new IllegalStateException("Gateway error"))
        .when(accountGateway).deleteById(eq(anAccount.getId().getValue()));

    Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(anAccount.getId().getValue()));

    Mockito.verify(accountGateway, times(1)).deleteById(eq(anAccount.getId().getValue()));
  }
}
