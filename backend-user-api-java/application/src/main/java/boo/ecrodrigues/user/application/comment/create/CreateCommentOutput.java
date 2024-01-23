package boo.ecrodrigues.user.application.comment.create;

import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.comment.Comment;
import boo.ecrodrigues.user.domain.comment.CommentID;

public record CreateCommentOutput(String accountId, String id) {

  public static CreateCommentOutput from(final AccountID anAccountID, final CommentID aCommentID) {
    return new CreateCommentOutput(anAccountID.getValue(), aCommentID.getValue());
  }

  public static CreateCommentOutput from(final Comment aComment) {
    return from(aComment.getAccountId(), aComment.getId());
  }
}
