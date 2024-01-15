package boo.ecrodrigues.user.infrastructure.account;

import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountJpaEntity;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import static boo.ecrodrigues.user.infrastructure.utils.SpecificationUtils.like;

@Component
public class AccountMongoGateway implements AccountGateway {

  private final AccountRepository repository;

  public AccountMongoGateway(final AccountRepository repository) {
    this.repository = repository;
  }

  @Override
  public Account create(Account anAccount) {
    return save(anAccount);
  }

  @Override
  public void deleteById(AccountID anId) {
    final String anIdValue = anId.getValue();
    if (this.repository.existsById(anIdValue)) {
      this.repository.deleteById(anIdValue);
    }
  }

  @Override
  public Optional<Account> findById(AccountID anId) {
    return this.repository.findById(anId.getValue())
        .map(AccountJpaEntity::toAggregate);
  }

  @Override
  public Account update(Account anAccount) {
    return save(anAccount);
  }

  @Override
  public Pagination<Account> findAll(SearchQuery aQuery) {
    // Pagination
    final var page = PageRequest.of(
        aQuery.page(),
        aQuery.perPage(),
        Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
    );

    //Dynamic search by terms (name or ...)
    final var specifications = Optional.ofNullable(aQuery.terms())
        .filter(str -> !str.isBlank())
        .map(this::assembleSpecification)
        .orElse(null);

    final var pageResult =
        this.repository.findAll(Specification.where(specifications), page);

    return new Pagination<>(
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        pageResult.map(AccountJpaEntity::toAggregate).toList()
    );
  }

  @Override
  public List<AccountID> existsByIds(Iterable<AccountID> accountIDs) {
    final var ids = StreamSupport.stream(accountIDs.spliterator(), false)
        .map(AccountID::getValue)
        .toList();
    return this.repository.existsByIds(ids).stream()
        .map(AccountID::from)
        .toList();
  }

  private Account save(final Account anAccount) {
    return this.repository.save(AccountJpaEntity.from(anAccount)).toAggregate();
  }

  private Specification<AccountJpaEntity> assembleSpecification(final String str) {
    final Specification<AccountJpaEntity> nameLike = like("name", str);
    return nameLike;
  }

}
