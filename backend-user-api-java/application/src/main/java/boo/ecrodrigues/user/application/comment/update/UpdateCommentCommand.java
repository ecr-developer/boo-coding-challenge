package boo.ecrodrigues.user.application.comment.update;

import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import java.util.Optional;

public record UpdateCommentCommand(
    String id,
    MBTI mbti,
    Enneagram enneagram,
    Zodiac zodiac,
    Integer like
) {

  public static UpdateCommentCommand with(
      final String anId,
      final MBTI aMbti,
      final Enneagram anEnneagram,
      final Zodiac aZodiac,
      final Integer aLike
  ) {
    return new UpdateCommentCommand(anId, aMbti, anEnneagram, aZodiac, aLike);
  }

  public static UpdateCommentCommand with(
      final String anId,
      final MBTI aMbti,
      final Enneagram anEnneagram,
      final Zodiac aZodiac
  ) {
    return new UpdateCommentCommand(anId, aMbti, anEnneagram, aZodiac, 0);
  }

  public static UpdateCommentCommand with(
      final String anId,
      final Integer aLike
  ) {
    return new UpdateCommentCommand(anId, null, null, null, aLike);
  }

  public Optional<MBTI> getMbti() {
    return Optional.ofNullable(mbti);
  }

  public Optional<Enneagram> getEnneagram() {
    return Optional.ofNullable(enneagram);
  }

  public Optional<Zodiac> getZodiac() {
    return Optional.ofNullable(zodiac);
  }

  public Optional<Integer> getLike() {
    return Optional.of(0);
  }
}
