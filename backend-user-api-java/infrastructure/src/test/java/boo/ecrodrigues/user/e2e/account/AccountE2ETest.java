package boo.ecrodrigues.user.e2e.account;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import boo.ecrodrigues.user.E2ETest;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.e2e.MockDsl;
import boo.ecrodrigues.user.infrastructure.account.models.UpdateAccountRequest;
import boo.ecrodrigues.user.infrastructure.account.persistence.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@E2ETest
@Testcontainers
public class AccountE2ETest implements MockDsl {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private AccountRepository accountRepository;

  @Container
  private static final MongoDBContainer MONGO_CONTAINER = new MongoDBContainer("mongo:4.4.20");

  @DynamicPropertySource
  public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
    registry.add("mongodb.port", () -> MONGO_CONTAINER.getMappedPort(27017));
  }

  @Override
  public MockMvc mvc() {
    return this.mvc;
  }

  @Test
  public void asAnAccountIShouldBeAbleToCreateANewAccountWithValidValues() throws Exception {
    Assertions.assertTrue(MONGO_CONTAINER.isRunning());
    Assertions.assertEquals(0, accountRepository.count());

    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    final var actualId = givenAnAccount(expectedName, expectedIsActive);

    final var actualCategory = accountRepository.findById(actualId.getValue()).get();

    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
    Assertions.assertNotNull(actualCategory.getCreatedAt());
    Assertions.assertNotNull(actualCategory.getUpdatedAt());
    Assertions.assertNull(actualCategory.getDeletedAt());
  }

  @Test
  public void asAnAccountIShouldBeAbleToNavigateToAllAccounts() throws Exception {
    Assertions.assertTrue(MONGO_CONTAINER.isRunning());
    Assertions.assertEquals(0, accountRepository.count());

    givenAnAccount("A Martinez", true);
    givenAnAccount("D Fernandez", true);
    givenAnAccount("E Rodrigues", true);

    listAccounts(0, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(0)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("A Martinez")));

    listAccounts(1, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(1)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("D Fernandez")));

    listAccounts(2, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(2)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("E Rodrigues")));

    listAccounts(3, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(3)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(0)));
  }

  @Test
  public void asAnAccountIShouldBeAbleToSearchBetweenAllAccounts() throws Exception {
    Assertions.assertTrue(MONGO_CONTAINER.isRunning());
    Assertions.assertEquals(0, accountRepository.count());

    givenAnAccount("A Martinez", true);
    givenAnAccount("D Fernandez", true);
    givenAnAccount("E Rodrigues", true);

    listAccounts(0, 1, "D")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(0)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(1)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("D Fernandez")));
  }

  @Test
  public void asAnAccountIShouldBeAbleToSortAllAccountsByNameDesc() throws Exception {
    Assertions.assertTrue(MONGO_CONTAINER.isRunning());
    Assertions.assertEquals(0, accountRepository.count());

    givenAnAccount("A Martinez", true);
    givenAnAccount("D Fernandez", true);
    givenAnAccount("E Rodrigues", true);

    listAccounts(0, 3, "", "name", "desc")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(0)))
        .andExpect(jsonPath("$.per_page", equalTo(3)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(3)))
        .andExpect(jsonPath("$.items[0].name", equalTo("E Rodrigues")))
        .andExpect(jsonPath("$.items[1].name", equalTo("D Fernandez")))
        .andExpect(jsonPath("$.items[2].name", equalTo("A Martinez")));
  }

  @Test
  public void asAnAccountIShouldBeAbleToGetAnAccountByItsIdentifier() throws Exception {
    Assertions.assertTrue(MONGO_CONTAINER.isRunning());
    Assertions.assertEquals(0, accountRepository.count());

    final var expectedName = "A Martinez";
    final var expectedIsActive = true;

    final var actualId = givenAnAccount(expectedName, expectedIsActive);

    final var actualAccount = retrieveAnAccount(actualId);

    Assertions.assertEquals(expectedName, actualAccount.name());
    Assertions.assertEquals(expectedIsActive, actualAccount.active());
    Assertions.assertNotNull(actualAccount.createdAt());
    Assertions.assertNotNull(actualAccount.updatedAt());
    Assertions.assertNull(actualAccount.deletedAt());
  }

  @Test
  public void asAnAccountIShouldBeAbleToUpdateAnAccountByItsIdentifier() throws Exception {
    Assertions.assertTrue(MONGO_CONTAINER.isRunning());
    Assertions.assertEquals(0, accountRepository.count());

    final var actualId = givenAnAccount("A Martinez", true);

    final var expectedName = "D Fernandez";
    final var expectedIsActive = true;

    final var aRequestBody = new UpdateAccountRequest(expectedName, expectedIsActive);

    updateAnAccount(actualId, aRequestBody)
        .andExpect(status().isOk());

    final var actualAccount = accountRepository.findById(actualId.getValue()).get();

    Assertions.assertEquals(expectedName, actualAccount.getName());
    Assertions.assertEquals(expectedIsActive, actualAccount.isActive());
    Assertions.assertNotNull(actualAccount.getCreatedAt());
    Assertions.assertNotNull(actualAccount.getUpdatedAt());
    Assertions.assertNull(actualAccount.getDeletedAt());
  }

  @Test
  public void asAnAccountIShouldBeAbleToInactivateAnAccountByItsIdentifier() throws Exception {
    Assertions.assertTrue(MONGO_CONTAINER.isRunning());
    Assertions.assertEquals(0, accountRepository.count());

    final var expectedName = "D Fernandez";
    final var expectedIsActive = false;

    final var actualId = givenAnAccount(expectedName, true);

    final var aRequestBody = new UpdateAccountRequest(expectedName, expectedIsActive);

    updateAnAccount(actualId, aRequestBody)
        .andExpect(status().isOk());

    final var actualAccount = accountRepository.findById(actualId.getValue()).get();

    Assertions.assertEquals(expectedName, actualAccount.getName());
    Assertions.assertEquals(expectedIsActive, actualAccount.isActive());
    Assertions.assertNotNull(actualAccount.getCreatedAt());
    Assertions.assertNotNull(actualAccount.getUpdatedAt());
    Assertions.assertNotNull(actualAccount.getDeletedAt());
  }

  @Test
  public void asAnAccountIShouldBeAbleToActivateAnAccountByItsIdentifier() throws Exception {
    Assertions.assertTrue(MONGO_CONTAINER.isRunning());
    Assertions.assertEquals(0, accountRepository.count());

    final var expectedName = "D Fernandez";
    final var expectedIsActive = true;

    final var actualId = givenAnAccount(expectedName, false);

    final var aRequestBody = new UpdateAccountRequest(expectedName, expectedIsActive);

    updateAnAccount(actualId, aRequestBody)
        .andExpect(status().isOk());

    final var actualAccount = accountRepository.findById(actualId.getValue()).get();

    Assertions.assertEquals(expectedName, actualAccount.getName());
    Assertions.assertEquals(expectedIsActive, actualAccount.isActive());
    Assertions.assertNotNull(actualAccount.getCreatedAt());
    Assertions.assertNotNull(actualAccount.getUpdatedAt());
    Assertions.assertNull(actualAccount.getDeletedAt());
  }

  @Test
  public void asAnAccountIShouldBeAbleToDeleteAnAccountByItsIdentifier() throws Exception {
    Assertions.assertTrue(MONGO_CONTAINER.isRunning());
    Assertions.assertEquals(0, accountRepository.count());

    final var actualId = givenAnAccount("D Fernandez", true);

    deleteAnAccount(actualId)
        .andExpect(status().isNoContent());

    final var actualAccount = accountRepository.findById(actualId.getValue()).get();

    Assertions.assertFalse(actualAccount.isActive());
  }

  @Test
  public void asAnAccountIShouldNotSeeAnErrorByDeletingANotExistentAccount() throws Exception {
    Assertions.assertTrue(MONGO_CONTAINER.isRunning());
    Assertions.assertEquals(0, accountRepository.count());

    deleteAnAccount(AccountID.from("12313"))
        .andExpect(status().isNoContent());

    Assertions.assertEquals(0, accountRepository.count());
  }
}
