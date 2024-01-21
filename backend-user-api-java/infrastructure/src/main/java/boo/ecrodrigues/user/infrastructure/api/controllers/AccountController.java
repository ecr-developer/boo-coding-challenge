package boo.ecrodrigues.user.infrastructure.api.controllers;

import boo.ecrodrigues.user.application.account.create.CreateAccountCommand;
import boo.ecrodrigues.user.application.account.create.CreateAccountOutput;
import boo.ecrodrigues.user.application.account.create.CreateAccountUseCase;
import boo.ecrodrigues.user.application.account.delete.DeleteAccountUseCase;
import boo.ecrodrigues.user.application.account.retrive.get.GetAccountByIdUseCase;
import boo.ecrodrigues.user.application.account.retrive.list.ListAccountsUseCase;
import boo.ecrodrigues.user.application.account.update.UpdateAccountCommand;
import boo.ecrodrigues.user.application.account.update.UpdateAccountOutput;
import boo.ecrodrigues.user.application.account.update.UpdateAccountUseCase;
import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;
import boo.ecrodrigues.user.domain.validation.handler.Notification;
import boo.ecrodrigues.user.infrastructure.account.models.AccountListResponse;
import boo.ecrodrigues.user.infrastructure.account.models.AccountResponse;
import boo.ecrodrigues.user.infrastructure.account.models.CreateAccountRequest;
import boo.ecrodrigues.user.infrastructure.account.models.UpdateAccountRequest;
import boo.ecrodrigues.user.infrastructure.account.presenters.AccountApiPresenter;
import boo.ecrodrigues.user.infrastructure.api.AccountAPI;
import java.net.URI;
import java.util.Objects;
import java.util.function.Function;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController implements AccountAPI {

  private final CreateAccountUseCase createAccountUseCase;
  private final GetAccountByIdUseCase getAccountByIdUseCase;
  private final UpdateAccountUseCase updateAccountUseCase;
  private final DeleteAccountUseCase deleteAccountUseCase;
  private final ListAccountsUseCase listAccountsUseCase;

  public AccountController(
      final CreateAccountUseCase createAccountUseCase,
      final GetAccountByIdUseCase getAccountByIdUseCase,
      final UpdateAccountUseCase updateAccountUseCase,
      final DeleteAccountUseCase deleteAccountUseCase,
      final ListAccountsUseCase listAccountsUseCase
  ) {
    this.createAccountUseCase = Objects.requireNonNull(createAccountUseCase);
    this.getAccountByIdUseCase = Objects.requireNonNull(getAccountByIdUseCase);
    this.updateAccountUseCase = Objects.requireNonNull(updateAccountUseCase);
    this.deleteAccountUseCase = Objects.requireNonNull(deleteAccountUseCase);
    this.listAccountsUseCase = Objects.requireNonNull(listAccountsUseCase);
  }

  @Override
  public ResponseEntity<?> createAccount(final CreateAccountRequest input) {
    final var aCommand = CreateAccountCommand.with(
        input.name(),
        input.active() != null ? input.active() : true
    );

    final Function<Notification, ResponseEntity<?>> onError = notification ->
        ResponseEntity.unprocessableEntity().body(notification);

    final Function<CreateAccountOutput, ResponseEntity<?>> onSuccess = output ->
        ResponseEntity.created(URI.create("/accounts/" + output.id())).body(output);

    return this.createAccountUseCase.execute(aCommand)
        .fold(onError, onSuccess);
  }

  @Override
  public Pagination<AccountListResponse> listAccounts(
      final String search,
      final int page,
      final int perPage,
      final String sort,
      final String direction
  ) {
    return listAccountsUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
        .map(AccountApiPresenter::present);
  }

  @Override
  public AccountResponse getById(final String id) {
    return AccountApiPresenter.present(this.getAccountByIdUseCase.execute(id));
  }

  @Override
  public ResponseEntity<?> updateById(final String id, final UpdateAccountRequest input) {
    final var aCommand = UpdateAccountCommand.with(
        id,
        input.name(),
        input.active() != null ? input.active() : true
    );

    final Function<Notification, ResponseEntity<?>> onError = notification ->
        ResponseEntity.unprocessableEntity().body(notification);

    final Function<UpdateAccountOutput, ResponseEntity<?>> onSuccess =
        ResponseEntity::ok;

    return this.updateAccountUseCase.execute(aCommand)
        .fold(onError, onSuccess);
  }

  @Override
  public void deleteById(final String anId) {
    this.deleteAccountUseCase.execute(anId);
  }
}
