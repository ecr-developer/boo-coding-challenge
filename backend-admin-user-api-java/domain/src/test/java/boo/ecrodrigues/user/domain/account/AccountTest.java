package boo.ecrodrigues.user.domain.account;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
public class AccountTest {

  @Test
  public void testNewAccount() {
    Assertions.assertNotNull(new Account());
  }

}
