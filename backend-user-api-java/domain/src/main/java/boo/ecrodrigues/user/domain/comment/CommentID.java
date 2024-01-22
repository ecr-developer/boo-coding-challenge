package boo.ecrodrigues.user.domain.comment;

import boo.ecrodrigues.user.domain.Identifier;
import boo.ecrodrigues.user.domain.utils.IdUtils;
import java.util.Objects;

public class CommentID extends Identifier {

  private final String value;

  private CommentID(final String anId) {
    Objects.requireNonNull(anId);
    this.value = anId;
  }

  public static CommentID unique() {
    return CommentID.from(IdUtils.uuid());
  }

  public static CommentID from(final String anId) {
    return new CommentID(anId);
  }

  @Override
  public String getValue() {
    return this.value;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final CommentID commentID = (CommentID) o;
    return Objects.equals(getValue(), commentID.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getValue());
  }
}
