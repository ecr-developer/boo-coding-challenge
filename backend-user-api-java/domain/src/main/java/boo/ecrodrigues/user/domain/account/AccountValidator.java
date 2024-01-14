package boo.ecrodrigues.user.domain.account;

import boo.ecrodrigues.user.domain.validation.Error;
import boo.ecrodrigues.user.domain.validation.ValidationHandler;
import boo.ecrodrigues.user.domain.validation.Validator;

public class AccountValidator extends Validator {

  public static final int NAME_MAX_LENGTH = 255;
  public static final int NAME_MIN_LENGTH = 3;
  private final Account account;

  public AccountValidator(final Account aAccount, final ValidationHandler aHandler) {
    super(aHandler);
    this.account = aAccount;
  }

  @Override
  public void validate() {
    checkNameConstraints();
  }

  private void checkNameConstraints() {
    final var name = this.account.getName();
    if (name == null) {
      this.validationHandler().append(new Error("'name' should not be null"));
      return;
    }

    if (name.isBlank()) {
      this.validationHandler().append(new Error("'name' should not be empty"));
      return;
    }

    final int length = name.trim().length();
    if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
      this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
    }
  }
}
