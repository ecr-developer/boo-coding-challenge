package boo.ecrodrigues.user.domain.validation.handler;

import boo.ecrodrigues.user.domain.exceptions.DomainException;
import boo.ecrodrigues.user.domain.validation.Error;
import boo.ecrodrigues.user.domain.validation.ValidationHandler;
import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

  @Override
  public ValidationHandler append(final Error anError) {
    throw DomainException.with(anError);
  }

  @Override
  public ValidationHandler append(final ValidationHandler anHandler) {
    throw DomainException.with(anHandler.getErrors());
  }

  @Override
  public <T> T validate(final Validation<T> aValidation) {
    try {
      return aValidation.validate();
    } catch (final Exception ex) {
      throw DomainException.with(new Error(ex.getMessage()));
    }
  }

  @Override
  public List<Error> getErrors() {
    return List.of();
  }
}