package boo.ecrodrigues.user.infrastructure.comment.persistence;

import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.comment.Comment;
import boo.ecrodrigues.user.domain.comment.CommentID;
import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "#{@dbCollections.getComment()}")
@CompoundIndexes({
    @CompoundIndex(name = "accountId_index", def = "{'accountId': 1}", unique = true, background = true)
})
public class CommentEntity {

  @Indexed(name = "_id_")
  @Id
  private String id;
  private String accountId;
  private String title;
  private String comment;
  private MBTI mbti;
  private Enneagram enneagram;
  private Zodiac zodiac;
  private Integer like;
  private Instant createdAt;
  private Instant updatedAt;

  public CommentEntity() {}
  public CommentEntity(
      final String id,
      final String accountId,
      final String title,
      final String comment,
      final MBTI mbti,
      final Enneagram enneagram,
      final Zodiac zodiac,
      final Integer like,
      final Instant createdAt,
      final Instant updatedAt
  ) {
    this.id = id;
    this.accountId = accountId;
    this.title = title;
    this.comment = comment;
    this.mbti = mbti;
    this.enneagram = enneagram;
    this.zodiac = zodiac;
    this.like = like;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public static CommentEntity from(final Comment aComment) {
    return new CommentEntity(
        aComment.getId().getValue(),
        aComment.getAccountId().getValue(),
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

  public Comment toAggregate() {
    return Comment.with(
        CommentID.from(getId()),
        AccountID.from(getAccountId()),
        getTitle(),
        getComment(),
        getMbti(),
        getEnneagram(),
        getZodiac(),
        getLike(),
        getCreatedAt(),
        getUpdatedAt()
    );
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public MBTI getMbti() {
    return mbti;
  }

  public void setMbti(MBTI mbti) {
    this.mbti = mbti;
  }

  public Enneagram getEnneagram() {
    return enneagram;
  }

  public void setEnneagram(Enneagram enneagram) {
    this.enneagram = enneagram;
  }

  public Zodiac getZodiac() {
    return zodiac;
  }

  public void setZodiac(Zodiac zodiac) {
    this.zodiac = zodiac;
  }

  public Integer getLike() {
    return like;
  }

  public void setLike(Integer like) {
    this.like = like;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}
