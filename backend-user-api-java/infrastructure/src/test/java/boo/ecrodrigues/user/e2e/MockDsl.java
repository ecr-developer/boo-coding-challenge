package boo.ecrodrigues.user.e2e;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import boo.ecrodrigues.user.domain.Identifier;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.comment.CommentID;
import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import boo.ecrodrigues.user.infrastructure.account.models.AccountResponse;
import boo.ecrodrigues.user.infrastructure.account.models.CreateAccountRequest;
import boo.ecrodrigues.user.infrastructure.account.models.UpdateAccountRequest;
import boo.ecrodrigues.user.infrastructure.comment.models.CommentResponse;
import boo.ecrodrigues.user.infrastructure.comment.models.CreateCommentRequest;
import boo.ecrodrigues.user.infrastructure.comment.models.UpdateCommentRequest;
import boo.ecrodrigues.user.infrastructure.configuration.json.Json;
import java.util.List;
import java.util.function.Function;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public interface MockDsl {

  MockMvc mvc();

  /**
   * Comment
   */
  default CommentID givenAComment(
      final String anAccountId,
      final String aTitle,
      final String aComment,
      final MBTI mbti,
      final Enneagram enneagram,
      final Zodiac zodiac
  ) throws Exception {
    final var aRequestBody = new CreateCommentRequest(anAccountId, aTitle, aComment, mbti, enneagram, zodiac);
    final var actualId = this.given("/comments", aRequestBody);
    return CommentID.from(actualId);
  }

  default ResultActions givenACommentResult(
      final String anAccountId,
      final String aTitle,
      final String aComment,
      final MBTI mbti,
      final Enneagram enneagram,
      final Zodiac zodiac
  ) throws Exception {
    final var aRequestBody = new CreateCommentRequest(anAccountId, aTitle, aComment, mbti, enneagram, zodiac);
    return this.givenResult("/comments", aRequestBody);
  }

  default ResultActions listComments(final int page, final int perPage) throws Exception {
    return listComments(page, perPage, "", "", "");
  }

  default ResultActions listComments(final int page, final int perPage, final String search) throws Exception {
    return listComments(page, perPage, search, "", "");
  }

  default ResultActions listComments(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
    return this.list("/comments", page, perPage, search, sort, direction);
  }

  default ResultActions listComments(final int page, final int perPage, final String sort, final String direction, final MBTI mbti, final Enneagram enneagram, final Zodiac zodiac) throws Exception {
    return this.list("/comments", page, perPage, sort, direction, mbti, enneagram, zodiac);
  }

  default ResultActions updateAComment(final CommentID anId, final Integer aLike) throws Exception {
    return this.update("/comments/", anId, new UpdateCommentRequest(aLike));
  }

  default CommentResponse retrieveAComment(final CommentID anId) throws Exception {
    return this.retrieve("/comments/", anId, CommentResponse.class);
  }

  /**
   * Account
   */
  default ResultActions deleteAnAccount(final AccountID anId) throws Exception {
    return this.delete("/accounts/", anId);
  }

  default AccountID givenAnAccount(final String aName, final boolean isActive) throws Exception {
    final var aRequestBody = new CreateAccountRequest(aName, isActive);
    final var actualId = this.given("/accounts", aRequestBody);
    return AccountID.from(actualId);
  }

  default ResultActions listAccounts(final int page, final int perPage) throws Exception {
    return listAccounts(page, perPage, "", "", "");
  }

  default ResultActions listAccounts(final int page, final int perPage, final String search) throws Exception {
    return listAccounts(page, perPage, search, "", "");
  }

  default ResultActions listAccounts(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
    return this.list("/accounts", page, perPage, search, sort, direction);
  }

  default AccountResponse retrieveAnAccount(final AccountID anId) throws Exception {
    return this.retrieve("/accounts/", anId, AccountResponse.class);
  }

  default ResultActions updateAnAccount(final AccountID anId, final UpdateAccountRequest aRequest) throws Exception {
    return this.update("/accounts/", anId, aRequest);
  }

  default <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper) {
    return actual.stream()
        .map(mapper)
        .toList();
  }

  private String given(final String url, final Object body) throws Exception {
    final var aRequest = post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(Json.writeValueAsString(body));

    final var actualId = this.mvc().perform(aRequest)
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse().getHeader("Location")
        .replace("%s/".formatted(url), "");

    return actualId;
  }

  private ResultActions givenResult(final String url, final Object body) throws Exception {
    final var aRequest = post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(Json.writeValueAsString(body));

    return this.mvc().perform(aRequest);
  }

  private ResultActions list(final String url, final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
    final var aRequest = get(url)
        .queryParam("page", String.valueOf(page))
        .queryParam("perPage", String.valueOf(perPage))
        .queryParam("search", search)
        .queryParam("sort", sort)
        .queryParam("dir", direction)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON);

    return this.mvc().perform(aRequest);
  }

  private ResultActions list(final String url, final int page, final int perPage, final String sort, final String direction, final MBTI mbti, final Enneagram enneagram, final Zodiac zodiac) throws Exception {
    final var aRequest = get(url)
        .queryParam("mbti", String.valueOf(mbti))
        .queryParam("enneagram", String.valueOf(enneagram))
        .queryParam("zodiac", String.valueOf(zodiac))
        .queryParam("page", String.valueOf(page))
        .queryParam("perPage", String.valueOf(perPage))
        .queryParam("sort", sort)
        .queryParam("dir", direction)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON);

    return this.mvc().perform(aRequest);
  }

  private <T> T retrieve(final String url, final Identifier anId, final Class<T> clazz) throws Exception {
    final var aRequest = get(url + anId.getValue())
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .contentType(MediaType.APPLICATION_JSON_UTF8);

    final var json = this.mvc().perform(aRequest)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse().getContentAsString();

    return Json.readValue(json, clazz);
  }

  private ResultActions retrieveResult(final String url, final Identifier anId) throws Exception {
    final var aRequest = get(url + anId.getValue())
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .contentType(MediaType.APPLICATION_JSON_UTF8);

    return this.mvc().perform(aRequest);
  }

  private ResultActions delete(final String url, final Identifier anId) throws Exception {
    final var aRequest = MockMvcRequestBuilders.delete(url + anId.getValue())
        .contentType(MediaType.APPLICATION_JSON);

    return this.mvc().perform(aRequest);
  }

  private ResultActions update(final String url, final Identifier anId, final Object aRequestBody) throws Exception {
    final var aRequest = put(url + anId.getValue())
        .contentType(MediaType.APPLICATION_JSON)
        .content(Json.writeValueAsString(aRequestBody));

    return this.mvc().perform(aRequest);
  }
}
