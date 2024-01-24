package boo.ecrodrigues.user.application.comment.retrive;

import boo.ecrodrigues.user.domain.comment.CommentGateway;
import boo.ecrodrigues.user.domain.pagination.Fields;
import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;
import java.util.Objects;

public non-sealed class DefaultListCommentsUseCase extends ListCommentUseCase {

  private final CommentGateway commentGateway;

  public DefaultListCommentsUseCase(final CommentGateway commentGateway) {
    this.commentGateway = Objects.requireNonNull(commentGateway);
  }

  @Override
  public Pagination<CommentListOutput> execute(final SearchQuery aQuery, final Fields fields) {
    return this.commentGateway.findAll(aQuery, fields)
        .map(CommentListOutput::from);
  }
}
