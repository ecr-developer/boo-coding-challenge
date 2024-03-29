package boo.ecrodrigues.user;

import boo.ecrodrigues.user.infrastructure.account.persistence.AccountRepository;
import boo.ecrodrigues.user.infrastructure.comment.persistence.CommentRepository;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class MongoCleanUpExtension implements BeforeEachCallback {

  @Override
  public void beforeEach(final ExtensionContext context) {
    final var appContext = SpringExtension.getApplicationContext(context);

    cleanUp(List.of(
        appContext.getBean(AccountRepository.class),
        appContext.getBean(CommentRepository.class)
    ));
  }

  private void cleanUp(final Collection<MongoRepository> repositories) {
    repositories.forEach(MongoRepository::deleteAll);
  }
}
