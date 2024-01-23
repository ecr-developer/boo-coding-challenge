package boo.ecrodrigues.user.application.comment.update;

import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.comment.Comment;
import boo.ecrodrigues.user.domain.comment.CommentID;

public record UpdateCommentOutput(String accountId, String id) {

  public static UpdateCommentOutput from(final AccountID anAccountID, final CommentID aCommentID) {
    return new UpdateCommentOutput(anAccountID.getValue(), aCommentID.getValue());
  }

  public static UpdateCommentOutput from(final Comment aComment) {
    return from(aComment.getAccountId(), aComment.getId());
  }
}
