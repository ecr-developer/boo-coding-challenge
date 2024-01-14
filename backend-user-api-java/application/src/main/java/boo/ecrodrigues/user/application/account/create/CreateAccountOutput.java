package boo.ecrodrigues.user.application.account.create;

import boo.ecrodrigues.user.domain.account.Account;

public record CreateAccountOutput(String id) {
  public static CreateAccountOutput from(final String anId) {
    return new CreateAccountOutput(anId);
  }

  public static CreateAccountOutput from(final Account aAccount) {
    return new CreateAccountOutput(aAccount.getId().getValue());
  }
}
