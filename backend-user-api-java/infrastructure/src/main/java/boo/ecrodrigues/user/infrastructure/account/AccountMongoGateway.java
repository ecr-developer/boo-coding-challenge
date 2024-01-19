package boo.ecrodrigues.user.infrastructure.account;

import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountEntity;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountRepository;
import boo.ecrodrigues.user.infrastructure.configuration.DatabaseCollectionsConfig;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class AccountMongoGateway implements AccountGateway {

  private static final Logger logger = LoggerFactory.getLogger(AccountMongoGateway.class);

  private static final String ACCOUNT_ID = "accountId";
  private static final String ACTIVE = "active";

  private static final String DELETED_FIELD_NAME = "deletedAt";

  private final MongoTemplate mongoTemplate;

  private final DatabaseCollectionsConfig collectionsConfig;

  private final AccountRepository accountRepository;

  @Autowired
  public AccountMongoGateway(
      final MongoTemplate mongoTemplate,
      final DatabaseCollectionsConfig collectionsConfig,
      final AccountRepository accountRepository
  ) {
    this.mongoTemplate = mongoTemplate;
    this.collectionsConfig = collectionsConfig;
    this.accountRepository = accountRepository;
  }

  @Override
  public Account create(Account anAccount) {
    final String collectionName = collectionsConfig.getAccount();
    return mongoTemplate.insert(AccountEntity.from(anAccount), collectionName).toAggregate();
  }

  @Override
  public Account update(Account anAccount) {
    final String accountCollection = collectionsConfig.getAccount();
    return mongoTemplate.save(AccountEntity.from(anAccount), accountCollection).toAggregate();
  }

  @Override
  public void deleteById(Account anAccount) {
    final String anIdValue = anAccount.getId().getValue();
    final Query query = new Query();
    query.addCriteria(Criteria.where(ACCOUNT_ID).is(anIdValue));

    final var update = new Update();
    update.set(ACTIVE, Boolean.FALSE);
    update.set("updatedAt", anAccount.getUpdatedAt());
    update.set("deletedAt", anAccount.getDeletedAt());

    final String collectionName = collectionsConfig.getAccount();
    mongoTemplate.findAndModify(query, update, AccountEntity.class, collectionName);
  }

  @Override
  public Optional<Account> findById(AccountID anId) {
    return this.accountRepository.findById(anId.getValue())
        .map(AccountEntity::toAggregate);
  }

  @Override
  public Pagination<Account> findAll(SearchQuery aQuery) {
    // Pagination
    final var pageRequest = PageRequest.of(
        aQuery.page(),
        aQuery.perPage()
    );

    // Dynamic search using terms (name or description)
    final var query = new Query();
    Optional.ofNullable(aQuery.terms())
        .filter(str -> !str.isBlank())
        .ifPresent(str -> {
          Criteria nameCriteria = Criteria.where("name").regex(str, "i");
          query.addCriteria(new Criteria().orOperator(nameCriteria));
        });

    query.with(pageRequest);

    final var pageResult = mongoTemplate.find(query, AccountEntity.class);
    long totalCount = mongoTemplate.count(query, AccountEntity.class);

    return new Pagination<>(
        pageRequest.getPageNumber(),
        pageRequest.getPageSize(),
        totalCount,
        pageResult.stream().map(AccountEntity::toAggregate).toList()
    );
  }
}
