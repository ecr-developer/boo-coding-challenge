package boo.ecrodrigues.user.domain.exceptions;

public class BadRequestException extends NoStacktraceException {

  protected BadRequestException(final String aMessage, final Throwable t) {
    super(aMessage, t);
  }

  public static BadRequestException with(final String message, final Throwable t) {
    return new BadRequestException(message, t);
  }

  public static BadRequestException with(final String message) {
    return new BadRequestException(message, null);
  }
}
