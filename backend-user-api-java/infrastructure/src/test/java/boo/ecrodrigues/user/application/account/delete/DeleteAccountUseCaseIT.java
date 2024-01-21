package boo.ecrodrigues.user.application.account.delete;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

import boo.ecrodrigues.user.IntegrationTest;
import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.utils.InstantUtils;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountEntity;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountRepository;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class DeleteAccountUseCaseIT {

  @Autowired
  private DeleteAccountUseCase useCase;

  @Autowired
  private AccountRepository accountRepository;

  @SpyBean
  private AccountGateway accountGateway;

  @Test
  public void givenAValidId_whenCallsDeleteAccount_shouldBeOK() {
    final var anAccount = Account.newAccount("A Martinez", true);
    final var expectedIsActive = false;

    save(anAccount);

    Assertions.assertEquals(1, accountRepository.count());

    Assertions.assertDoesNotThrow(() -> useCase.execute(anAccount.getId().getValue()));

    final var actualAccount =
        accountRepository.findById(anAccount.getId().getValue()).get();

    Assertions.assertEquals(1, accountRepository.count());
    Assertions.assertEquals(expectedIsActive, actualAccount.isActive());
  }

  @Test
  public void givenAInvalidId_whenCallsDeleteAccount_shouldBeOK() {
    final var anAccount = Account.with(
        AccountID.from("123"),
        "A Martinez",
        true,
        InstantUtils.now(),
        InstantUtils.now(),
        null
    );

    Assertions.assertEquals(0, accountRepository.count());

    Assertions.assertDoesNotThrow(() -> useCase.execute(anAccount.getId().getValue()));

    Assertions.assertEquals(0, accountRepository.count());
  }

  @Test
  public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
    final var anAccount = Account.newAccount("A Martinez", true);

    doThrow(new IllegalStateException("Gateway error"))
        .when(accountGateway).deleteById(eq(anAccount.getId().getValue()));

    Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(anAccount.getId().getValue()));

    Mockito.verify(accountGateway, times(1)).deleteById(eq(anAccount.getId().getValue()));
  }

  private void save(final Account... anAccount) {
    accountRepository.saveAll(
        Arrays.stream(anAccount)
            .map(AccountEntity::from)
            .toList()
    );
  }
}
