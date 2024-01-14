package boo.ecrodrigues.user.application.account.retrive.list;

import boo.ecrodrigues.user.domain.account.AccountGateway;
import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;
import java.util.Objects;

public class DefaultListAccountsUseCase extends ListAccountsUseCase {

  private final AccountGateway accountGateway;

  public DefaultListAccountsUseCase(final AccountGateway accountGateway) {
    this.accountGateway = Objects.requireNonNull(accountGateway);
  }

  @Override
  public Pagination<AccountListOutput> execute(final SearchQuery aQuery) {
    return this.accountGateway.findAll(aQuery)
        .map(AccountListOutput::from);
  }
}
