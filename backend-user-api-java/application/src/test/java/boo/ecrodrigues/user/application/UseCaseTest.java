package boo.ecrodrigues.user.application;

import boo.ecrodrigues.user.domain.Identifier;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
public abstract class UseCaseTest implements BeforeEachCallback {

  @Override
  public void beforeEach(final ExtensionContext context) throws Exception {
    Mockito.reset(getMocks().toArray());
  }

  protected abstract List<Object> getMocks();

  protected Set<String> asString(final Set<? extends Identifier> ids) {
    return ids.stream()
        .map(Identifier::getValue)
        .collect(Collectors.toSet());
  }

  protected List<String> asString(final List<? extends Identifier> ids) {
    return ids.stream()
        .map(Identifier::getValue)
        .toList();
  }
}
