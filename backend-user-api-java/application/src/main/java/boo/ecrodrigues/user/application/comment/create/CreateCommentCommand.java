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
    Zodiac zodiac,
    Integer like
) {

  public static CreateCommentCommand with(
      final AccountID accountId,
      final String title,
      final String comment,
      final MBTI mbti,
      final Enneagram enneagram,
      final Zodiac zodiac,
      final Integer like
  ) {
    return new CreateCommentCommand(
        accountId,
        title,
        comment,
        mbti,
        enneagram,
        zodiac,
        like
    );
  }

  public static CreateCommentCommand with(
      final AccountID accountId,
      final String title,
      final String comment
  ) {
    return new CreateCommentCommand(
        accountId,
        title,
        comment,
        null,
        null,
        null,
        0
    );
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
