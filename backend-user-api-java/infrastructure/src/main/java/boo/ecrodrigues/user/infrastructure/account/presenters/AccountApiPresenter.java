package boo.ecrodrigues.user.infrastructure.account.presenters;

import boo.ecrodrigues.user.application.account.retrive.get.AccountOutput;
import boo.ecrodrigues.user.application.account.retrive.list.AccountListOutput;
import boo.ecrodrigues.user.infrastructure.account.models.AccountListResponse;
import boo.ecrodrigues.user.infrastructure.account.models.AccountResponse;

public interface AccountApiPresenter {

  static AccountResponse present(final AccountOutput output) {
    return new AccountResponse(
        output.id().getValue(),
        output.name(),
        output.isActive(),
        output.createdAt(),
        output.updatedAt(),
        output.deletedAt()
    );
  }

  static AccountListResponse present(final AccountListOutput output) {
    return new AccountListResponse(
        output.id().getValue(),
        output.name(),
        output.isActive(),
        output.createdAt(),
        output.deletedAt()
    );
  }
}
