package boo.ecrodrigues.user.application.comment.update;

import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import java.util.Optional;

public record UpdateCommentCommand(
    String id,
    Integer like
) {

  public static UpdateCommentCommand with(
      final String anId,
      final Integer aLike
  ) {
    return new UpdateCommentCommand(anId, aLike);
  }
}
