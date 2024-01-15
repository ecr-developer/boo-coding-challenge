package boo.ecrodrigues.user.infrastructure.account.persistence;

import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountID;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "accounts")
public class AccountJpaEntity {

  @Indexed(name = "_id_")
  @Id
  private String id;

  @Field(order = 1)
  private String name;

  @Field(order = 2)
  private boolean active;

  @Field(order = 3, name = "created_at")
  private Instant createdAt;

  @Field(order = 4, name = "updated_at")
  private Instant updatedAt;

  @Field(order = 5, name = "deleted_at")
  private Instant deletedAt;

  public AccountJpaEntity() {
  }

  private AccountJpaEntity(
      final String id,
      final String name,
      final boolean active,
      final Instant createdAt,
      final Instant updatedAt,
      final Instant deletedAt
  ) {
    this.id = id;
    this.name = name;
    this.active = active;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
  }

  public static AccountJpaEntity from(final Account anAccount) {
    return new AccountJpaEntity(
        anAccount.getId().getValue(),
        anAccount.getName(),
        anAccount.isActive(),
        anAccount.getCreatedAt(),
        anAccount.getUpdatedAt(),
        anAccount.getDeletedAt()
    );
  }

  public Account toAggregate() {
    return Account.with(
        AccountID.from(getId()),
        getName(),
        isActive(),
        getCreatedAt(),
        getUpdatedAt(),
        getDeletedAt()
    );
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Instant getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(Instant deletedAt) {
    this.deletedAt = deletedAt;
  }
}
