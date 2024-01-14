package boo.ecrodrigues.user.application.account.update;

import boo.ecrodrigues.user.domain.account.Account;

public record UpdateAccountOutput(String id) {

  public static UpdateAccountOutput from(final String anId) {
    return new UpdateAccountOutput(anId);
  }

  public static UpdateAccountOutput from(final Account anAccount) {
    return new UpdateAccountOutput(anAccount.getId().getValue());
  }
}
