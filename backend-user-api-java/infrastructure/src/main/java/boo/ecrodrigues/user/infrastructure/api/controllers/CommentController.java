package boo.ecrodrigues.user.infrastructure.api.controllers;

import boo.ecrodrigues.user.application.comment.create.CreateCommentCommand;
import boo.ecrodrigues.user.application.comment.create.CreateCommentUseCase;
import boo.ecrodrigues.user.application.comment.retrive.ListCommentUseCase;
import boo.ecrodrigues.user.application.comment.update.UpdateCommentCommand;
import boo.ecrodrigues.user.application.comment.update.UpdateCommentOutput;
import boo.ecrodrigues.user.application.comment.update.UpdateCommentUseCase;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.pagination.Fields;
import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;
import boo.ecrodrigues.user.infrastructure.api.CommentAPI;
import boo.ecrodrigues.user.infrastructure.comment.models.CommentListResponse;
import boo.ecrodrigues.user.infrastructure.comment.models.CreateCommentRequest;
import boo.ecrodrigues.user.infrastructure.comment.models.UpdateCommentRequest;
import boo.ecrodrigues.user.infrastructure.comment.presenters.CommentPresenter;
import java.net.URI;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController implements CommentAPI {

  private final CreateCommentUseCase createCommentUseCase;
  private final UpdateCommentUseCase updateCommentUseCase;
  private final ListCommentUseCase listCommentUseCase;

  public CommentController(
      final CreateCommentUseCase createCommentUseCase,
      final UpdateCommentUseCase updateCommentUseCase,
      final ListCommentUseCase listCommentUseCase
  ) {
    this.createCommentUseCase = Objects.requireNonNull(createCommentUseCase);
    this.updateCommentUseCase = Objects.requireNonNull(updateCommentUseCase);
    this.listCommentUseCase = Objects.requireNonNull(listCommentUseCase);
  }

  @Override
  public ResponseEntity<?> create(final CreateCommentRequest input) {
    final var aCommand = CreateCommentCommand.with(
        AccountID.from(input.accountId()),
        input.title(),
        input.comment()
    );

    final var output = this.createCommentUseCase.execute(aCommand);

    return ResponseEntity.created(URI.create("/comments/" + output.id())).body(output);
  }

  @Override
  public ResponseEntity<?> updateById(final String id, final UpdateCommentRequest aBody) {
    final var aCommand = UpdateCommentCommand.with(
        id,
        aBody.mbti() != null ? aBody.mbti() : null,
        aBody.enneagram() != null ? aBody.enneagram() : null,
        aBody.zodiac() != null ? aBody.zodiac() : null,
        aBody.like() > 0 ? aBody.like() : 0
    );

    final var output = this.updateCommentUseCase.execute(aCommand);
    return ResponseEntity.ok(output);
  }

  @Override
  public Pagination<CommentListResponse> list(
      final String search,
      final String mbti,
      final String enneagram,
      final String zodiac,
      final int page,
      final int perPage,
      final String sort,
      final String direction
  ) {
    final var searchQuery = new SearchQuery(
        page,
        perPage,
        search,
        sort,
        direction
    );

    final var fields = new Fields(
        mbti != null && StringUtils.isNotBlank(mbti) ? mbti : null,
        enneagram != null && StringUtils.isNotBlank(enneagram) ? enneagram : null,
        zodiac != null && StringUtils.isNotBlank(zodiac) ? zodiac : null
    );

    return this.listCommentUseCase.execute(searchQuery, fields).map(CommentPresenter::present);
  }
}
