package boo.ecrodrigues.user.infrastructure.api;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import boo.ecrodrigues.user.ControllerTest;
import boo.ecrodrigues.user.application.comment.create.CreateCommentOutput;
import boo.ecrodrigues.user.application.comment.create.DefaultCreateCommentUseCase;
import boo.ecrodrigues.user.application.comment.retrive.CommentListOutput;
import boo.ecrodrigues.user.application.comment.retrive.DefaultListCommentsUseCase;
import boo.ecrodrigues.user.application.comment.update.DefaultUpdateCommentUseCase;
import boo.ecrodrigues.user.application.comment.update.UpdateCommentOutput;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.comment.Comment;
import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import boo.ecrodrigues.user.domain.exceptions.NotificationException;
import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.infrastructure.comment.models.CreateCommentRequest;
import boo.ecrodrigues.user.domain.validation.Error;
import boo.ecrodrigues.user.infrastructure.comment.models.UpdateCommentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ControllerTest(controllers = CommentAPI.class)
public class CommentAPITest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private DefaultCreateCommentUseCase createCommentUseCase;

  @MockBean
  private DefaultListCommentsUseCase listCommentsUseCase;

  @MockBean
  private DefaultUpdateCommentUseCase updateCommentUseCase;

  @Test
  public void givenAValidCommand_whenCallsCreateComment_shouldReturnItsIdentifier() throws Exception {
    // given
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    final var aCommand =
        new CreateCommentRequest(expectedAccountID.getValue(), expectedTitle, expectedComment);

    when(createCommentUseCase.execute(any()))
        .thenReturn(CreateCommentOutput.from(aComment));

    // when
    final var aRequest = post("/comments")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(aCommand));

    final var response = this.mvc.perform(aRequest)
        .andDo(print());

    // then
    response.andExpect(status().isCreated())
        .andExpect(header().string("Location", "/comments/" + aComment.getId().getValue()))
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id", equalTo(aComment.getId().getValue())));

    verify(createCommentUseCase).execute(argThat(actualCmd ->
        Objects.equals(expectedTitle, actualCmd.title())
            && Objects.equals(expectedComment, actualCmd.comment())
    ));
  }

  @Test
  public void givenAValidCommand_whenCallsUpdateComment_shouldReturnItsIdentifier() throws Exception {
    // given
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);
    final var expectedId = aComment.getId();

    final var aCommand =
        new UpdateCommentRequest(MBTI.ESTJ, Enneagram.E_4W3, Zodiac.Gemini, 0);

    when(updateCommentUseCase.execute(any()))
        .thenReturn(UpdateCommentOutput.from(aComment));

    // when
    final var aRequest = put("/comments/{id}", expectedId.getValue())
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(aCommand));

    final var response = this.mvc.perform(aRequest)
        .andDo(print());

    // then
    response.andExpect(status().isOk())
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

    verify(updateCommentUseCase).execute(argThat(actualCmd ->
        Objects.equals(expectedId.getValue(), actualCmd.id())
    ));
  }

  @Test
  public void givenValidParams_whenCallListComments_shouldReturnIt() throws Exception {
    // given
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    final var expectedPage = 1;
    final var expectedPerPage = 20;
    final var expectedTerms = "";
    final var expectedSort = "like";
    final var expectedDirection = "desc";

    final var expectedItemsCount = 1;
    final var expectedTotal = 1;

    final var expectedItems = List.of(CommentListOutput.from(aComment));

    when(listCommentsUseCase.execute(any(), any()))
        .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

    // when
    final var aRequest = get("/comments")
        .queryParam("page", String.valueOf(expectedPage))
        .queryParam("perPage", String.valueOf(expectedPerPage))
        .queryParam("search", expectedTerms)
        .queryParam("sort", expectedSort)
        .queryParam("dir", expectedDirection)
        .accept(MediaType.APPLICATION_JSON);

    final var response = this.mvc.perform(aRequest);

    // then
    response.andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
        .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
        .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
        .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
        .andExpect(jsonPath("$.items[0].id", equalTo(aComment.getId().getValue())))
        .andExpect(jsonPath("$.items[0].title", equalTo(aComment.getTitle())))
        .andExpect(jsonPath("$.items[0].comment", equalTo(aComment.getComment())))
        .andExpect(jsonPath("$.items[0].created_at", equalTo(aComment.getCreatedAt().toString())));
  }
}
