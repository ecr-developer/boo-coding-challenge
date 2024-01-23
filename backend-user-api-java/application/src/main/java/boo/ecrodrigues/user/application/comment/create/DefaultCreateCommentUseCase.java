package boo.ecrodrigues.user.application.comment.create;

import boo.ecrodrigues.user.domain.comment.Comment;
import boo.ecrodrigues.user.domain.comment.CommentGateway;
import boo.ecrodrigues.user.domain.exceptions.NotificationException;
import boo.ecrodrigues.user.domain.validation.handler.Notification;
import java.util.Objects;

public non-sealed class DefaultCreateCommentUseCase extends CreateCommentUseCase {

  private final CommentGateway commentGateway;

  public DefaultCreateCommentUseCase(final CommentGateway commentGateway) {
    this.commentGateway = Objects.requireNonNull(commentGateway);
  }

  @Override
  public CreateCommentOutput execute(final CreateCommentCommand aComment) {
    final var anAccountId = aComment.accountId();
    final var aTitle = aComment.title();
    final var acomment = aComment.comment();

    final var notification = Notification.create();

    final var aNewComment = notification.validate(() -> Comment.newComment(anAccountId, aTitle, acomment));

    if (notification.hasError()) {
      notify(notification);
    }

    return CreateCommentOutput.from(this.commentGateway.create(aNewComment));
  }

  private void notify(Notification notification) {
    throw new NotificationException("Could not create Aggregate Comment", notification);
  }
}
