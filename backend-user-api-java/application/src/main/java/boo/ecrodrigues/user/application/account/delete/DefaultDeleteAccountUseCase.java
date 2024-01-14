package boo.ecrodrigues.user.application.account.delete;

import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.domain.account.AccountID;
import java.util.Objects;

public class DefaultDeleteAccountUseCase extends DeleteAccountUseCase {

  private final AccountGateway accountGateway;

  public DefaultDeleteAccountUseCase(final AccountGateway categoryGateway) {
    this.accountGateway = Objects.requireNonNull(categoryGateway);
  }

  @Override
  public void execute(final String anIn) {
    this.accountGateway.deleteById(AccountID.from(anIn));
  }
}
