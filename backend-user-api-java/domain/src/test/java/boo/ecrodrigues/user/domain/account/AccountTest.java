package boo.ecrodrigues.user.domain.account;

import static org.junit.jupiter.api.Assertions.*;

import boo.ecrodrigues.user.domain.UnitTest;
import boo.ecrodrigues.user.domain.exceptions.DomainException;
import boo.ecrodrigues.user.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTest extends UnitTest {

  @Test
  public void givenAValidParams_whenCallNewAccount_thenInstantiateAnAccount() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    final var actualAccount =
        Account.newAccount(expectedName, expectedIsActive);

    assertNotNull(actualAccount);
    assertNotNull(actualAccount.getId());
    assertEquals(expectedName, actualAccount.getName());
    assertEquals(expectedIsActive, actualAccount.isActive());
    assertNotNull(actualAccount.getCreatedAt());
    assertNotNull(actualAccount.getUpdatedAt());
    assertNull(actualAccount.getDeletedAt());
  }

  @Test
  public void givenAnInvalidNullName_whenCallNewAccountAndValidate_thenShouldReceiveError() {
    final String expectedName = null;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be null";
    final var expectedIsActive = true;

    final var actualAccount =
        Account.newAccount(expectedName, expectedIsActive);

    final var actualException =
        assertThrows(DomainException.class, () -> actualAccount.validate(new ThrowsValidationHandler()));

    assertEquals(expectedErrorCount, actualException.getErrors().size());
    assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAnInvalidEmptyName_whenCallNewAccountAndValidate_thenShouldReceiveError() {
    final var expectedName = "  ";
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be empty";
    final var expectedIsActive = true;

    final var actualAccount =
        Account.newAccount(expectedName, expectedIsActive);

    final var actualException =
        assertThrows(DomainException.class, () -> actualAccount.validate(new ThrowsValidationHandler()));

    assertEquals(expectedErrorCount, actualException.getErrors().size());
    assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAnInvalidNameLengthLessThan3_whenCallNewAccountAndValidate_thenShouldReceiveError() {
    final var expectedName = "Fi ";
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
    final var expectedIsActive = true;

    final var actualAccount =
        Account.newAccount(expectedName, expectedIsActive);

    final var actualException =
        assertThrows(DomainException.class, () -> actualAccount.validate(new ThrowsValidationHandler()));

    assertEquals(expectedErrorCount, actualException.getErrors().size());
    assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAnInvalidNameLengthMoreThan255_whenCallNewAccountAndValidate_thenShouldReceiveError() {
    final var expectedName = """
              Lorem ipsum dolor sit amet, consectetur adipiscing elit.
              Vestibulum quis pellentesque est. Phasellus rhoncus, augue id lobortis mattis, purus leo rhoncus dolor, vel commodo quam neque id lectus.
              Sed quis pretium purus, sollicitudin molestie enim. Nullam dolor.
              """;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
    final var expectedIsActive = true;

    final var actualAccount =
        Account.newAccount(expectedName, expectedIsActive);

    final var actualException =
        assertThrows(DomainException.class, () -> actualAccount.validate(new ThrowsValidationHandler()));

    assertEquals(expectedErrorCount, actualException.getErrors().size());
    assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAValidFalseIsActive_whenCallNewAccountAndValidate_thenShouldReceiveOK() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = false;

    final var actualAccount =
        Account.newAccount(expectedName, expectedIsActive);

    assertDoesNotThrow(() -> actualAccount.validate(new ThrowsValidationHandler()));

    assertNotNull(actualAccount);
    assertNotNull(actualAccount.getId());
    assertEquals(expectedName, actualAccount.getName());
    assertEquals(expectedIsActive, actualAccount.isActive());
    assertNotNull(actualAccount.getCreatedAt());
    assertNotNull(actualAccount.getUpdatedAt());
    assertNotNull(actualAccount.getDeletedAt());
  }

  @Test
  public void givenAValidActiveAccount_whenCallDeactivate_thenReturnAccountInactivated() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = false;

    final var anAccount =
        Account.newAccount(expectedName, true);

    assertDoesNotThrow(() -> anAccount.validate(new ThrowsValidationHandler()));

    final var createdAt = anAccount.getCreatedAt();
    final var updatedAt = anAccount.getUpdatedAt();

    assertTrue(anAccount.isActive());
    assertNull(anAccount.getDeletedAt());

    final var actualAccount = anAccount.deactivate();

    assertDoesNotThrow(() -> actualAccount.validate(new ThrowsValidationHandler()));

    assertEquals(anAccount.getId(), actualAccount.getId());
    assertEquals(expectedName, actualAccount.getName());
    assertEquals(expectedIsActive, actualAccount.isActive());
    assertEquals(createdAt, actualAccount.getCreatedAt());
    assertNotNull(actualAccount.getDeletedAt());
  }

  @Test
  public void givenAValidInactiveAccount_whenCallActivate_thenReturnAccountActivated() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    final var anAccount =
        Account.newAccount(expectedName, false);

    assertDoesNotThrow(() -> anAccount.validate(new ThrowsValidationHandler()));

    final var createdAt = anAccount.getCreatedAt();
    final var updatedAt = anAccount.getUpdatedAt();

    assertFalse(anAccount.isActive());
    assertNotNull(anAccount.getDeletedAt());

    final var actualAccount = anAccount.activate();

    assertDoesNotThrow(() -> actualAccount.validate(new ThrowsValidationHandler()));

    assertEquals(anAccount.getId(), actualAccount.getId());
    assertEquals(expectedName, actualAccount.getName());
    assertEquals(expectedIsActive, actualAccount.isActive());
    assertEquals(createdAt, actualAccount.getCreatedAt());
    assertNull(actualAccount.getDeletedAt());
  }

  @Test
  public void givenAValidAccount_whenCallUpdate_thenReturnAccountUpdated() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    final var aAccount =
        Account.newAccount("G Fernandez", expectedIsActive);

    assertDoesNotThrow(() -> aAccount.validate(new ThrowsValidationHandler()));

    final var createdAt = aAccount.getCreatedAt();
    final var updatedAt = aAccount.getUpdatedAt();

    final var actualAccount = aAccount.update(expectedName, expectedIsActive);

    assertDoesNotThrow(() -> actualAccount.validate(new ThrowsValidationHandler()));

    assertEquals(aAccount.getId(), actualAccount.getId());
    assertEquals(expectedName, actualAccount.getName());
    assertEquals(expectedIsActive, actualAccount.isActive());
    assertEquals(createdAt, actualAccount.getCreatedAt());
    assertTrue(actualAccount.getUpdatedAt().isAfter(updatedAt));
    assertNull(actualAccount.getDeletedAt());
  }

  @Test
  public void givenAValidAccount_whenCallUpdateToInactive_thenReturnAccountUpdated() {
    final var expectedName = "A Martinez";
    final var expectedIsActive = false;

    final var aAccount =
        Account.newAccount("G Fernandez", true);

    assertDoesNotThrow(() -> aAccount.validate(new ThrowsValidationHandler()));
    assertTrue(aAccount.isActive());
    assertNull(aAccount.getDeletedAt());

    final var createdAt = aAccount.getCreatedAt();
    final var updatedAt = aAccount.getUpdatedAt();

    final var actualAccount = aAccount.update(expectedName, expectedIsActive);

    assertDoesNotThrow(() -> actualAccount.validate(new ThrowsValidationHandler()));

    assertEquals(aAccount.getId(), actualAccount.getId());
    assertEquals(expectedName, actualAccount.getName());
    assertFalse(aAccount.isActive());
    assertEquals(createdAt, actualAccount.getCreatedAt());
    assertTrue(actualAccount.getUpdatedAt().isAfter(updatedAt));
    assertNotNull(aAccount.getDeletedAt());
  }

  @Test
  public void givenAValidAccount_whenCallUpdateWithInvalidParams_thenReturnAccountUpdated() {
    final String expectedName = null;
    final var expectedIsActive = true;

    final var aAccount =
        Account.newAccount("G Fernandez", expectedIsActive);

    Assertions.assertDoesNotThrow(() -> aAccount.validate(new ThrowsValidationHandler()));

    final var createdAt = aAccount.getCreatedAt();
    final var updatedAt = aAccount.getUpdatedAt();

    final var actualAccount = aAccount.update(expectedName, expectedIsActive);

    Assertions.assertEquals(aAccount.getId(), actualAccount.getId());
    Assertions.assertEquals(expectedName, actualAccount.getName());
    Assertions.assertTrue(aAccount.isActive());
    Assertions.assertEquals(createdAt, actualAccount.getCreatedAt());
    Assertions.assertTrue(actualAccount.getUpdatedAt().isAfter(updatedAt));
    Assertions.assertNull(aAccount.getDeletedAt());
  }
}
