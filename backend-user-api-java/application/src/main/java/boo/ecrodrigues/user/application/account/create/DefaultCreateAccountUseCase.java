package boo.ecrodrigues.user.application.account.create;

import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.domain.validation.handler.Notification;
import io.vavr.control.Either;
import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultCreateAccountUseCase extends CreateAccountUseCase {

  private final AccountGateway accountGateway;

  public DefaultCreateAccountUseCase(AccountGateway accountGateway) {
    this.accountGateway = Objects.requireNonNull(accountGateway);
  }

  @Override
  public Either<Notification, CreateAccountOutput> execute(final CreateAccountCommand aCommand) {
    final var aName = aCommand.name();
    final var isActive = aCommand.isActive();

    final var notification = Notification.create();

    final var aAccount = Account.newAccount(aName, isActive);
    aAccount.validate(notification);

    return notification.hasError() ? Left(notification) : create(aAccount);
  }

  private Either<Notification, CreateAccountOutput> create(final Account aAccount) {
    return Try(() -> this.accountGateway.create(aAccount))
        .toEither()
        .bimap(Notification::create, CreateAccountOutput::from);
  }
}
