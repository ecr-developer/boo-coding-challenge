package boo.ecrodrigues.user.application.account.create;

import boo.ecrodrigues.user.application.UseCase;
import boo.ecrodrigues.user.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateAccountUseCase
    extends UseCase<CreateAccountCommand, Either<Notification, CreateAccountOutput>> {
}
