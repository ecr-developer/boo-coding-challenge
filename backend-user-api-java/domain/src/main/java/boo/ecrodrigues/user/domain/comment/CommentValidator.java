package boo.ecrodrigues.user.domain.comment;

import boo.ecrodrigues.user.domain.validation.Error;
import boo.ecrodrigues.user.domain.validation.ValidationHandler;
import boo.ecrodrigues.user.domain.validation.Validator;

public class CommentValidator extends Validator {

  public static final int TITLE_MAX_LENGTH = 255;
  public static final int TITLE_MIN_LENGTH = 3;
  public static final int COMMENT_MAX_LENGTH = 4000;
  public static final int COMMENT_MIN_LENGTH = 3;

  private final Comment comment;

  public CommentValidator(final Comment aComment, final ValidationHandler aHandler) {
    super(aHandler);
    this.comment = aComment;
  }

  @Override
  public void validate() {
    checkTitleConstraints();
    checkPersonalityType();
    checkCommentConstraints();
  }

  private void checkTitleConstraints() {
    final var title = this.comment.getTitle();
    if (title == null) {
      this.validationHandler().append(new Error("'title' should not be null"));
      return;
    }

    if (title.isBlank()) {
      this.validationHandler().append(new Error("'title' should not be empty"));
      return;
    }

    final int length = title.trim().length();
    if (length > TITLE_MAX_LENGTH || length < TITLE_MIN_LENGTH) {
      this.validationHandler().append(new Error("'title' must be between 3 and 255 characters"));
    }
  }

  private void checkPersonalityType() {
    final var mbti = this.comment.getMbti();
    final var enneagram = this.comment.getEnneagram();
    final var zodiac = this.comment.getZodiac();

    if (mbti == null) {
      this.validationHandler().append(new Error("'mbti' should not be null"));
      return;
    }

    if (enneagram == null) {
      this.validationHandler().append(new Error("'enneagram' should not be null"));
      return;
    }

    if (zodiac == null) {
      this.validationHandler().append(new Error("'zodiac' should not be null"));
      return;
    }
  }

  private void checkCommentConstraints() {
    final var comment = this.comment.getComment();
    if (comment == null) {
      this.validationHandler().append(new Error("'comment' should not be null"));
      return;
    }

    if (comment.isBlank()) {
      this.validationHandler().append(new Error("'comment' should not be empty"));
      return;
    }

    final int length = comment.trim().length();
    if (length > COMMENT_MAX_LENGTH || length < COMMENT_MIN_LENGTH) {
      this.validationHandler().append(new Error("'comment' must be between 3 and 4000 characters"));
    }
  }
}
