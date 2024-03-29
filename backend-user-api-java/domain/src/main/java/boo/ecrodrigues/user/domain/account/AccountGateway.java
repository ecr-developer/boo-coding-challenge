package boo.ecrodrigues.user.domain.account;

import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;
import java.util.List;
import java.util.Optional;

public interface AccountGateway {

  Account create(Account anAccount);

  void deleteById(String anId);

  Optional<Account> findById(AccountID anId);

  Account update(Account anAccount);

  Pagination<Account> findAll(SearchQuery aQuery);
}
