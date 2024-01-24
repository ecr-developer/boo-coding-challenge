package boo.ecrodrigues.user.application.comment.update;

import boo.ecrodrigues.user.domain.Identifier;
import boo.ecrodrigues.user.domain.comment.Comment;
import boo.ecrodrigues.user.domain.comment.CommentGateway;
import boo.ecrodrigues.user.domain.comment.CommentID;
import boo.ecrodrigues.user.domain.exceptions.NotFoundException;
import boo.ecrodrigues.user.domain.exceptions.NotificationException;
import boo.ecrodrigues.user.domain.validation.handler.Notification;
import java.util.Objects;
import java.util.function.Supplier;

public non-sealed class DefaultUpdateCommentUseCase extends UpdateCommentUseCase {

  private final CommentGateway commentGateway;

  public DefaultUpdateCommentUseCase(final CommentGateway commentGateway) {
    this.commentGateway = Objects.requireNonNull(commentGateway);
  }

  @Override
  public UpdateCommentOutput execute(UpdateCommentCommand aCommand) {
    final var anId = CommentID.from(aCommand.id());
    final var aLike = aCommand.like();

    final var aComment = this.commentGateway.findById(anId)
        .orElseThrow(notFound(anId));

    final var notification = Notification.create();
    notification.validate(() -> aComment.update(aLike));

    if (notification.hasError()) {
      notify(anId, notification);
    }

    return UpdateCommentOutput.from(this.commentGateway.update(aComment));
  }

  private void notify(final Identifier anId, final Notification notification) {
    throw new NotificationException("Could not update Aggregate Comment %s".formatted(anId.getValue()), notification);
  }
  private Supplier<NotFoundException> notFound(final CommentID anId) {
    return () -> NotFoundException.with(Comment.class, anId);
  }
}
