package boo.ecrodrigues.user.application.account.update;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.exceptions.DomainException;
import boo.ecrodrigues.user.domain.exceptions.NotFoundException;
import boo.ecrodrigues.user.domain.validation.handler.Notification;
import io.vavr.control.Either;
import java.util.Objects;
import java.util.function.Supplier;

public class DefaultUpdateAccountUseCase extends UpdateAccountUseCase {

  private final AccountGateway accountGateway;

  public DefaultUpdateAccountUseCase(final AccountGateway accountGateway) {
    this.accountGateway = Objects.requireNonNull(accountGateway);
  }

  @Override
  public Either<Notification, UpdateAccountOutput> execute(final UpdateAccountCommand aCommand) {
    final var anId = AccountID.from(aCommand.id());
    final var aName = aCommand.name();
    final var isActive = aCommand.isActive();

    final var anAccount = this.accountGateway.findById(anId)
        .orElseThrow(notFound(anId));

    final var notification = Notification.create();
    anAccount
        .update(aName, isActive)
        .validate(notification);

    return notification.hasError() ? Left(notification) : update(anAccount);
  }

  private Either<Notification, UpdateAccountOutput> update(final Account anAccount) {
    return Try(() -> this.accountGateway.update(anAccount))
        .toEither()
        .bimap(Notification::create, UpdateAccountOutput::from);
  }

  private Supplier<DomainException> notFound(final AccountID anId) {
    return () -> NotFoundException.with(Account.class, anId);
  }
}
