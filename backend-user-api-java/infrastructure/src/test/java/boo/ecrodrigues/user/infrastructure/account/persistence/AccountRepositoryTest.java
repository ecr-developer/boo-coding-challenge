package boo.ecrodrigues.user.infrastructure.account.persistence;

import boo.ecrodrigues.user.MongoGatewayTest;
import boo.ecrodrigues.user.domain.account.Account;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MongoGatewayTest
public class AccountRepositoryTest {
  @Autowired
  private AccountRepository accountRepository;

  @Test
  public void givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
    final var expectedPropertyName = "name";
    final var expectedMessage = "not-null property references a null or transient value : boo.ecrodrigues.user.infrastructure.account.persistence.AccountJpaEntity.name";

    final var anAccount = Account.newAccount("A Martinez",true);

    final var anEntity = AccountJpaEntity.from(anAccount);
    anEntity.setName(null);

    final var actualException =
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> accountRepository.save(anEntity));

    final var actualCause =
        Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

    Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
    Assertions.assertEquals(expectedMessage, actualCause.getMessage());
  }

  @Test
  public void givenAnInvalidNullCreatedAt_whenCallsSave_shouldReturnError() {
    final var expectedPropertyName = "createdAt";
    final var expectedMessage = "not-null property references a null or transient value : boo.ecrodrigues.user.infrastructure.account.persistence.AccountJpaEntity.createdAt";

    final var anAccount = Account.newAccount("A Martinez",true);

    final var anEntity = AccountJpaEntity.from(anAccount);
    anEntity.setCreatedAt(null);

    final var actualException =
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> accountRepository.save(anEntity));

    final var actualCause =
        Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

    Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
    Assertions.assertEquals(expectedMessage, actualCause.getMessage());
  }

  @Test
  public void givenAnInvalidNullUpdatedAt_whenCallsSave_shouldReturnError() {
    final var expectedPropertyName = "updatedAt";
    final var expectedMessage = "not-null property references a null or transient value : boo.ecrodrigues.user.infrastructure.account.persistence.AccountJpaEntity.updatedAt";

    final var anAccount = Account.newAccount("A Martinez",true);

    final var anEntity = AccountJpaEntity.from(anAccount);
    anEntity.setUpdatedAt(null);

    final var actualException =
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> accountRepository.save(anEntity));

    final var actualCause =
        Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

    Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
    Assertions.assertEquals(expectedMessage, actualCause.getMessage());
  }
}