package boo.ecrodrigues.user.application.comment.create;

import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import java.util.Optional;

public record CreateCommentCommand(
    AccountID accountId,
    String title,
    String comment,
    MBTI mbti,
    Enneagram enneagram,
    Zodiac zodiac
) {

  public static CreateCommentCommand with(
      final AccountID accountId,
      final String title,
      final String comment,
      final MBTI mbti,
      final Enneagram enneagram,
      final Zodiac zodiac
  ) {
    return new CreateCommentCommand(
        accountId,
        title,
        comment,
        mbti,
        enneagram,
        zodiac
    );
  }
}
