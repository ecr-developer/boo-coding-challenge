package boo.ecrodrigues.user.domain.account;

import boo.ecrodrigues.user.domain.AggregateRoot;
import boo.ecrodrigues.user.domain.utils.InstantUtils;
import boo.ecrodrigues.user.domain.validation.ValidationHandler;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Account extends AggregateRoot<AccountID> implements Cloneable {
  private String name;
  private boolean active;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant deletedAt;

  private Account(
      final AccountID anId,
      final String aName,
      final boolean isActive,
      final Instant aCreationDate,
      final Instant aUpdateDate,
      final Instant aDeleteDate
  ) {
    super(anId);
    this.name = aName;
    this.active = isActive;
    this.createdAt = Objects.requireNonNull(aCreationDate, "'createdAt' should not be null");
    this.updatedAt = Objects.requireNonNull(aUpdateDate, "'updatedAt' should not be null");
    this.deletedAt = aDeleteDate;
  }

  public static Account newAccount(final String aName, final boolean isActive) {
    final var id = AccountID.unique();
    final var now = InstantUtils.now();
    final var deletedAt = isActive ? null : now;
    return new Account(id, aName, isActive, now, now, deletedAt);
  }

  public static Account with(
      final AccountID anId,
      final String name,
      final boolean active,
      final Instant createdAt,
      final Instant updatedAt,
      final Instant deletedAt
  ) {
    return new Account(
        anId,
        name,
        active,
        createdAt,
        updatedAt,
        deletedAt
    );
  }

  public static Account with(final Account aAccount) {
    return with(
        aAccount.getId(),
        aAccount.name,
        aAccount.isActive(),
        aAccount.createdAt,
        aAccount.updatedAt,
        aAccount.deletedAt
    );
  }

  @Override
  public void validate(final ValidationHandler handler) {
    new AccountValidator(this, handler).validate();
  }

  public Account activate() {
    this.deletedAt = null;
    this.active = true;
    this.updatedAt = InstantUtils.now();
    return this;
  }

  public Account deactivate() {
    if (getDeletedAt() == null) {
      this.deletedAt = InstantUtils.now();
    }

    this.active = false;
    this.updatedAt = InstantUtils.now();
    return this;
  }

  public Account update(
      final String aName,
      final boolean isActive
  ) {
    if (isActive) {
      activate();
    } else {
      deactivate();
    }
    this.name = aName;
    this.updatedAt = Instant.now();
    return this;
  }

  public AccountID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public boolean isActive() {
    return active;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public Instant getDeletedAt() {
    return deletedAt;
  }

  @Override
  public Account clone() {
    try {
      return (Account) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }
}
