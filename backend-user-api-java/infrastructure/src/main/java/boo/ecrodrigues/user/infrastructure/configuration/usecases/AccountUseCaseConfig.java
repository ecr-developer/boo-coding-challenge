package boo.ecrodrigues.user.infrastructure.configuration.usecases;

import boo.ecrodrigues.user.application.account.create.CreateAccountUseCase;
import boo.ecrodrigues.user.application.account.create.DefaultCreateAccountUseCase;
import boo.ecrodrigues.user.application.account.delete.DeleteAccountUseCase;
import boo.ecrodrigues.user.application.account.retrive.get.DefaultGetAccountByIdUseCase;
import boo.ecrodrigues.user.application.account.retrive.get.GetAccountByIdUseCase;
import boo.ecrodrigues.user.application.account.retrive.list.DefaultListAccountsUseCase;
import boo.ecrodrigues.user.application.account.retrive.list.ListAccountsUseCase;
import boo.ecrodrigues.user.application.account.update.DefaultUpdateAccountUseCase;
import boo.ecrodrigues.user.application.account.update.UpdateAccountUseCase;
import boo.ecrodrigues.user.domain.account.AccountGateway;
import org.springframework.context.annotation.Bean;

public class AccountUseCaseConfig {

  private final AccountGateway accountGateway;

  public AccountUseCaseConfig(final AccountGateway accountGateway) {
    this.accountGateway = accountGateway;
  }

  @Bean
  public CreateAccountUseCase createAccountUseCase() {
    return new DefaultCreateAccountUseCase(accountGateway);
  }

  @Bean
  public UpdateAccountUseCase updateAccountUseCase() {
    return new DefaultUpdateAccountUseCase(accountGateway);
  }

  @Bean
  public GetAccountByIdUseCase getAccountByIdUseCase() {
    return new DefaultGetAccountByIdUseCase(accountGateway);
  }

  @Bean
  public ListAccountsUseCase listAccountsUseCase() {
    return new DefaultListAccountsUseCase(accountGateway);
  }

  @Bean
  public DeleteAccountUseCase deleteAccountUseCase() {
    return new DeleteAccountUseCase(accountGateway);
  }
}
