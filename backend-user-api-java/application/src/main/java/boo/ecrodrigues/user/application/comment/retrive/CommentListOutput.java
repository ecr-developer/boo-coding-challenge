package boo.ecrodrigues.user.application.comment.retrive;

import boo.ecrodrigues.user.domain.comment.Comment;
import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import java.time.Instant;

public record CommentListOutput(
    String accountId,
    String id,
    String title,
    String comment,
    MBTI mbti,
    Enneagram enneagram,
    Zodiac zodiac,
    Integer like,
    Instant createdAt,
    Instant updatedAt
) {

  public static CommentListOutput from(final Comment aComment) {
    return new CommentListOutput(
        aComment.getAccountId().getValue(),
        aComment.getId().getValue(),
        aComment.getTitle(),
        aComment.getComment(),
        aComment.getMbti(),
        aComment.getEnneagram(),
        aComment.getZodiac(),
        aComment.getLike(),
        aComment.getCreatedAt(),
        aComment.getUpdatedAt()
    );
  }
}
