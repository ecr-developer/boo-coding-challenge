package boo.ecrodrigues.user.infrastructure.account;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;
import boo.ecrodrigues.user.domain.utils.InstantUtils;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountEntity;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountRepository;
import boo.ecrodrigues.user.infrastructure.configuration.DatabaseCollectionsConfig;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class AccountMongoGateway implements AccountGateway {

  private static final Logger logger = LoggerFactory.getLogger(AccountMongoGateway.class);

  private static final String ACCOUNT_ID = "_id";
  private static final String ACTIVE = "active";

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
  public void deleteById(final String anId) {
    final Query query = new Query();
    query.addCriteria(where(ACCOUNT_ID).is(anId));

    final var update = new Update();
    update.set(ACTIVE, Boolean.FALSE);
    update.set("updatedAt", InstantUtils.now());
    update.set("deletedAt", InstantUtils.now());

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
    final var page = PageRequest.of(
        aQuery.page(),
        aQuery.perPage(),
        Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
    );

    Page<AccountEntity> pageResult = accountRepository.findByNameRegexIgnoreCase("^" + aQuery.terms(), page);

    return new Pagination<>(
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        pageResult.map(AccountEntity::toAggregate).toList()
    );
  }
}
