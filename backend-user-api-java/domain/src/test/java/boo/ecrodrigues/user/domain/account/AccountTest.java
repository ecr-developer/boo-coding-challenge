package boo.ecrodrigues.user.domain.account;

import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTest {

  @Test
  public void givenAValidParams_whenCallNewAccount_thenInstantiateAnAccount() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    final var actualAccount =
        Account.newAccount(expectedName, expectedIsActive);

    Assertions.assertNotNull(actualAccount);
    Assertions.assertNotNull(UUID.randomUUID().toString());
    Assertions.assertEquals(expectedName, actualAccount.getName());
    Assertions.assertEquals(expectedIsActive, actualAccount.isActive());
    Assertions.assertNotNull(actualAccount.getCreatedAt());
    Assertions.assertNotNull(actualAccount.getUpdatedAt());
    Assertions.assertNull(actualAccount.getDeletedAt());
  }

}
