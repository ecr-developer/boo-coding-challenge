package boo.ecrodrigues.user.application.account.update;

import boo.ecrodrigues.user.application.UseCase;
import boo.ecrodrigues.user.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateAccountUseCase extends
    UseCase<UpdateAccountCommand, Either<Notification, UpdateAccountOutput>> {

}
