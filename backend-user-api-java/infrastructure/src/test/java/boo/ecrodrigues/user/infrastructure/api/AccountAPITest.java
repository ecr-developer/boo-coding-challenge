package boo.ecrodrigues.user.infrastructure.api;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import boo.ecrodrigues.user.ControllerTest;
import boo.ecrodrigues.user.application.account.create.CreateAccountOutput;
import boo.ecrodrigues.user.application.account.create.CreateAccountUseCase;
import boo.ecrodrigues.user.application.account.delete.DeleteAccountUseCase;
import boo.ecrodrigues.user.application.account.retrive.get.AccountOutput;
import boo.ecrodrigues.user.application.account.retrive.get.GetAccountByIdUseCase;
import boo.ecrodrigues.user.application.account.retrive.list.AccountListOutput;
import boo.ecrodrigues.user.application.account.retrive.list.ListAccountsUseCase;
import boo.ecrodrigues.user.application.account.update.UpdateAccountOutput;
import boo.ecrodrigues.user.application.account.update.UpdateAccountUseCase;
import boo.ecrodrigues.user.domain.account.Account;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.exceptions.DomainException;
import boo.ecrodrigues.user.domain.exceptions.NotFoundException;
import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.validation.Error;
import boo.ecrodrigues.user.domain.validation.handler.Notification;
import boo.ecrodrigues.user.infrastructure.account.models.CreateAccountRequest;
import boo.ecrodrigues.user.infrastructure.account.models.UpdateAccountRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ControllerTest(controllers = AccountAPI.class)
public class AccountAPITest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private CreateAccountUseCase createAccountUseCase;

  @MockBean
  private GetAccountByIdUseCase getAccountByIdUseCase;

  @MockBean
  private UpdateAccountUseCase updateAccountUseCase;

  @MockBean
  private DeleteAccountUseCase deleteAccountUseCase;

  @MockBean
  private ListAccountsUseCase listAccountsUseCase;

  @Test
  public void givenAValidCommand_whenCallsCreateAccount_shouldReturnAccountId() throws Exception {
    // given
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    final var anInput =
        new CreateAccountRequest(expectedName, expectedIsActive);

    when(createAccountUseCase.execute(any()))
        .thenReturn(Right(CreateAccountOutput.from("123")));

    // when
    final var request = post("/accounts")
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.mapper.writeValueAsString(anInput));

    final var response = this.mvc.perform(request)
        .andDo(print());

    // then
    response.andExpect(status().isCreated())
        .andExpect(header().string("Location", "/accounts/123"))
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id", equalTo("123")));

    verify(createAccountUseCase, times(1)).execute(argThat(cmd ->
        Objects.equals(expectedName, cmd.name())
            && Objects.equals(expectedIsActive, cmd.isActive())
    ));
  }

  @Test
  public void givenAInvalidName_whenCallsCreateAccount_thenShouldReturnNotification() throws Exception {
    // given
    final String expectedName = null;
    final var expectedIsActive = true;
    final var expectedMessage = "'name' should not be null";

    final var anInput =
        new CreateAccountRequest(expectedName, expectedIsActive);

    when(createAccountUseCase.execute(any()))
        .thenReturn(Left(Notification.create(new Error(expectedMessage))));

    // when
    final var request = post("/accounts")
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.mapper.writeValueAsString(anInput));

    final var response = this.mvc.perform(request)
        .andDo(print());

    // then
    response.andExpect(status().isUnprocessableEntity())
        .andExpect(header().string("Location", nullValue()))
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errors", hasSize(1)))
        .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

    verify(createAccountUseCase, times(1)).execute(argThat(cmd ->
        Objects.equals(expectedName, cmd.name())
            && Objects.equals(expectedIsActive, cmd.isActive())
    ));
  }

  @Test
  public void givenAValidId_whenCallsGetAccount_shouldReturnAccount() throws Exception {
    // given
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    final var anAccount = Account.newAccount(expectedName, expectedIsActive);

    final var expectedId = anAccount.getId().getValue();

    when(getAccountByIdUseCase.execute(any()))
        .thenReturn(AccountOutput.from(anAccount));

    // when
    final var request = get("/accounts/{id}", expectedId)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON);

    final var response = this.mvc.perform(request)
        .andDo(print());

    // then
    response.andExpect(status().isOk())
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id", equalTo(expectedId)))
        .andExpect(jsonPath("$.name", equalTo(expectedName)))
        .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
        .andExpect(jsonPath("$.created_at", equalTo(anAccount.getCreatedAt().toString())))
        .andExpect(jsonPath("$.updated_at", equalTo(anAccount.getUpdatedAt().toString())))
        .andExpect(jsonPath("$.deleted_at", equalTo(anAccount.getDeletedAt())));

    verify(getAccountByIdUseCase, times(1)).execute(eq(expectedId));
  }

  @Test
  public void givenAValidCommand_whenCallsUpdateAccount_shouldReturnAccountId() throws Exception {
    // given
    final var expectedId = "123";
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    when(updateAccountUseCase.execute(any()))
        .thenReturn(Right(UpdateAccountOutput.from(expectedId)));

    final var aCommand =
        new UpdateAccountRequest(expectedName, expectedIsActive);

    // when
    final var request = put("/accounts/{id}", expectedId)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(aCommand));

    final var response = this.mvc.perform(request)
        .andDo(print());

    // then
    response.andExpect(status().isOk())
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id", equalTo(expectedId)));

    verify(updateAccountUseCase, times(1)).execute(argThat(cmd ->
        Objects.equals(expectedName, cmd.name())
            && Objects.equals(expectedIsActive, cmd.isActive())
    ));
  }

  @Test
  public void givenAInvalidName_whenCallsUpdateAccount_thenShouldReturnDomainException() throws Exception {
    // given
    final var expectedId = "123";
    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    final var expectedErrorCount = 1;
    final var expectedMessage = "'name' should not be null";

    when(updateAccountUseCase.execute(any()))
        .thenReturn(Left(Notification.create(new Error(expectedMessage))));

    final var aCommand =
        new UpdateAccountRequest(expectedName, expectedIsActive);

    // when
    final var request = put("/accounts/{id}", expectedId)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(aCommand));

    final var response = this.mvc.perform(request)
        .andDo(print());

    // then
    response.andExpect(status().isUnprocessableEntity())
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
        .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

    verify(updateAccountUseCase, times(1)).execute(argThat(cmd ->
        Objects.equals(expectedName, cmd.name())
            && Objects.equals(expectedIsActive, cmd.isActive())
    ));
  }

  @Test
  public void givenAValidId_whenCallsDeleteAccount_shouldReturnNoContent() throws Exception {
    // given
    final var expectedId = "123";

    doNothing()
        .when(deleteAccountUseCase).execute(any());

    // when
    final var request = delete("/accounts/{id}", expectedId)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON);

    final var response = this.mvc.perform(request)
        .andDo(print());

    // then
    response.andExpect(status().isNoContent());

    verify(deleteAccountUseCase, times(1)).execute(eq(expectedId));
  }

  @Test
  public void givenValidParams_whenCallsListAccounts_shouldReturnAccounts() throws Exception {
    // given
    final var anAccount = Account.newAccount("A Martinez",true);

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "A";
    final var expectedSort = "name";
    final var expectedDirection = "desc";
    final var expectedItemsCount = 1;
    final var expectedTotal = 1;

    final var expectedItems = List.of(AccountListOutput.from(anAccount));

    when(listAccountsUseCase.execute(any()))
        .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

    // when
    final var request = get("/accounts")
        .queryParam("page", String.valueOf(expectedPage))
        .queryParam("perPage", String.valueOf(expectedPerPage))
        .queryParam("sort", expectedSort)
        .queryParam("dir", expectedDirection)
        .queryParam("search", expectedTerms)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON);

    final var response = this.mvc.perform(request)
        .andDo(print());

    // then
    response.andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
        .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
        .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
        .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
        .andExpect(jsonPath("$.items[0].id", equalTo(anAccount.getId().getValue())))
        .andExpect(jsonPath("$.items[0].name", equalTo(anAccount.getName())))
        .andExpect(jsonPath("$.items[0].is_active", equalTo(anAccount.isActive())))
        .andExpect(jsonPath("$.items[0].created_at", equalTo(anAccount.getCreatedAt().toString())))
        .andExpect(jsonPath("$.items[0].deleted_at", equalTo(anAccount.getDeletedAt())));

    verify(listAccountsUseCase, times(1)).execute(argThat(query ->
        Objects.equals(expectedPage, query.page())
            && Objects.equals(expectedPerPage, query.perPage())
            && Objects.equals(expectedDirection, query.direction())
            && Objects.equals(expectedSort, query.sort())
            && Objects.equals(expectedTerms, query.terms())
    ));
  }
}
