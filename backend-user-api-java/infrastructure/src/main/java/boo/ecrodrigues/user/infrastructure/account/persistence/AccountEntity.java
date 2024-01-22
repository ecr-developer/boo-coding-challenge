package boo.ecrodrigues.user.infrastructure.account.persistence;

import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountID;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "#{@dbCollections.getAccount()}")
@CompoundIndexes({
    @CompoundIndex(name = "name_index", def = "{'name': 1}")
})
public class AccountEntity {

  @Indexed(name = "_id_")
  @Id
  private String id;
  private String name;
  private boolean active;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant deletedAt;

  public AccountEntity() {
  }

  private AccountEntity(
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

  public static AccountEntity from(final Account anAccount) {
    return new AccountEntity(
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
