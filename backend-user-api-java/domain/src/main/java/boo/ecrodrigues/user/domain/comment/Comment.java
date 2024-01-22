package boo.ecrodrigues.user.domain.comment;

import boo.ecrodrigues.user.domain.AggregateRoot;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.exceptions.NotificationException;
import boo.ecrodrigues.user.domain.utils.InstantUtils;
import boo.ecrodrigues.user.domain.validation.ValidationHandler;
import boo.ecrodrigues.user.domain.validation.handler.Notification;
import java.time.Instant;

public class Comment extends AggregateRoot<CommentID> {

  private AccountID accountId;
  private String title;
  private String comment;
  private MBTI mbti;
  private Enneagram enneagram;
  private Zodiac zodiac;
  private Integer like;
  private Instant createdAt;
  private Instant updatedAt;

  protected Comment(
      final CommentID anId,
      final AccountID anAccountId,
      final String aTitle,
      final String aComment,
      final MBTI aMbti,
      final Enneagram anEnneagram,
      final Zodiac aZodiac,
      final Integer aLike,
      final Instant aCreatedAt,
      final Instant anUpdatedAt
  ) {
    super(anId);
    this.accountId = anAccountId;
    this.title = aTitle;
    this.comment = aComment;
    this.mbti = aMbti;
    this.enneagram = anEnneagram;
    this.zodiac = aZodiac;
    this.like = aLike;
    this.createdAt = aCreatedAt;
    this.updatedAt = anUpdatedAt;
    selfValidate();
  }

  public static Comment newComment(
      final AccountID anAccountId,
      final String aTitle,
      final String aComment
  ) {
    final var anId = CommentID.unique();
    final var now = InstantUtils.now();
    return new Comment(anId, anAccountId, aTitle, aComment, null, null, null, 0, now, now);
  }

  public static Comment with(
      final CommentID anId,
      final AccountID anAccountId,
      final String aTitle,
      final String aComment,
      final Instant aCreationDate,
      final Instant anUpdateDate
  ) {
    return new Comment(anId, anAccountId, aTitle, aComment, null, null, null, 0, aCreationDate, anUpdateDate);
  }

  public static Comment with(final Comment aComment) {
    return new Comment(
        aComment.id,
        aComment.accountId,
        aComment.title,
        aComment.comment,
        aComment.mbti,
        aComment.enneagram,
        aComment.zodiac,
        aComment.like,
        aComment.createdAt,
        aComment.updatedAt
    );
  }

  public Comment update(
      final MBTI aMbti,
      final Enneagram anEnneagram,
      final Zodiac aZodiac
  ) {
    this.mbti = aMbti;
    this.enneagram = anEnneagram;
    this.zodiac = aZodiac;
    this.updatedAt = InstantUtils.now();
    selfValidate();
    return this;
  }

  public Comment update(
      final Integer aLike
  ) {
    if(aLike > 0) {
      like();
    } else {
      dislike();
    }
    selfValidate();
    return this;
  }

  public Comment like() {
    this.like += 1;
    this.updatedAt = InstantUtils.now();
    return this;
  }

  public Comment dislike() {
    if (this.like > 0) {
      this.like -= 1;
      this.updatedAt = InstantUtils.now();
    }
    return this;
  }

  public AccountID getAccountId() {
    return accountId;
  }

  public String getTitle() {
    return title;
  }

  public String getComment() {
    return comment;
  }

  public MBTI getMbti() {
    return mbti;
  }

  public Enneagram getEnneagram() {
    return enneagram;
  }

  public Zodiac getZodiac() {
    return zodiac;
  }

  public Integer getLike() {
    return like;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public void validate(ValidationHandler handler) {
//    new CommentValidator(this, handler).validate();
  }

  private void selfValidate() {
    final var notification = Notification.create();
    validate(notification);

    if (notification.hasError()) {
      throw new NotificationException("Failed to create a Aggregate Comment", notification);
    }
  }
}
