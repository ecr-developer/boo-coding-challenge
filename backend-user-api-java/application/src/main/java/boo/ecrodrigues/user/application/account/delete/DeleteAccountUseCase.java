package boo.ecrodrigues.user.application.account.delete;

import boo.ecrodrigues.user.application.UnitUseCase;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.domain.account.AccountID;
import java.util.Objects;

public class DeleteAccountUseCase extends UnitUseCase<String> {

  private final AccountGateway accountGateway;

  public DeleteAccountUseCase(final AccountGateway accountGateway) {
    this.accountGateway = Objects.requireNonNull(accountGateway);
  }

  @Override
  public void execute(final String anIn) {
    if (anIn == null) {
      return;
    }

    this.accountGateway.deleteById(AccountID.from(anIn));
  }
}
