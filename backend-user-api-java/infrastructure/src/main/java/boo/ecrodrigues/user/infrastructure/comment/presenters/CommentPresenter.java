package boo.ecrodrigues.user.infrastructure.comment.presenters;

import boo.ecrodrigues.user.application.comment.retrive.CommentListOutput;
import boo.ecrodrigues.user.infrastructure.comment.models.CommentListResponse;

public interface CommentPresenter {

  static CommentListResponse present(final CommentListOutput aComment) {
    return new CommentListResponse(
        aComment.accountId(),
        aComment.id(),
        aComment.title(),
        aComment.comment(),
        aComment.mbti(),
        aComment.enneagram(),
        aComment.zodiac(),
        aComment.like(),
        aComment.createdAt(),
        aComment.updatedAt()
    );
  }
}
