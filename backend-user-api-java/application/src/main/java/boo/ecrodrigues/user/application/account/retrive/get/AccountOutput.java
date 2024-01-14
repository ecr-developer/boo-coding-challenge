package boo.ecrodrigues.user.application.account.retrive.get;

import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountID;
import java.time.Instant;

public record AccountOutput(
    AccountID id,
    String name,
    boolean isActive,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt
) {
  public static AccountOutput from(final Account anAccount) {
    return new AccountOutput(
        anAccount.getId(),
        anAccount.getName(),
        anAccount.isActive(),
        anAccount.getCreatedAt(),
        anAccount.getUpdatedAt(),
        anAccount.getDeletedAt()
    );
  }
}
