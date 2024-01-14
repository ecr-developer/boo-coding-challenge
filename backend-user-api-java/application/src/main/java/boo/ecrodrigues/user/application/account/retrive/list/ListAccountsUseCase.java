package boo.ecrodrigues.user.application.account.retrive.list;

import boo.ecrodrigues.user.application.UseCase;
import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;

public abstract class ListAccountsUseCase extends
    UseCase<SearchQuery, Pagination<AccountListOutput>> {
}
