package boo.ecrodrigues.user.application.account.retrive.list;

import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountID;
import java.time.Instant;

public record AccountListOutput(
    AccountID id,
    String name,
    boolean isActive,
    Instant createdAt,
    Instant deletedAt
) {

  public static AccountListOutput from(final Account anAccount) {
    return new AccountListOutput(
        anAccount.getId(),
        anAccount.getName(),
        anAccount.isActive(),
        anAccount.getCreatedAt(),
        anAccount.getDeletedAt()
    );
  }
}
