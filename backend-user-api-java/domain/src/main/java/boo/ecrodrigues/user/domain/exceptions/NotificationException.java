package boo.ecrodrigues.user.domain.exceptions;

import boo.ecrodrigues.user.domain.validation.handler.Notification;

public class NotificationException extends DomainException {

  public NotificationException(final String aMessage, final Notification notification) {
    super(aMessage, notification.getErrors());
  }
}
