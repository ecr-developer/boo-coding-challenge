package boo.ecrodrigues.user.application.account.retrive.get;

import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.exceptions.NotFoundException;
import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetAccountByIdUseCase extends GetAccountByIdUseCase {

  private final AccountGateway accountGateway;

  public DefaultGetAccountByIdUseCase(final AccountGateway accountGateway) {
    this.accountGateway = Objects.requireNonNull(accountGateway);
  }

  @Override
  public AccountOutput execute(final String anIn) {
    final var anAccountID = AccountID.from(anIn);

    return this.accountGateway.findById(anAccountID)
        .map(AccountOutput::from)
        .orElseThrow(notFound(anAccountID));
  }

  private Supplier<NotFoundException> notFound(final AccountID anId) {
    return () -> NotFoundException.with(Account.class, anId);
  }
}
