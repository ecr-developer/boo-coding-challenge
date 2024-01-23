package boo.ecrodrigues.user.infrastructure.configuration.usecases;

import boo.ecrodrigues.user.application.comment.create.CreateCommentUseCase;
import boo.ecrodrigues.user.application.comment.create.DefaultCreateCommentUseCase;
import boo.ecrodrigues.user.application.comment.retrive.DefaultListCommentsUseCase;
import boo.ecrodrigues.user.application.comment.retrive.ListCommentUseCase;
import boo.ecrodrigues.user.application.comment.update.DefaultUpdateCommentUseCase;
import boo.ecrodrigues.user.application.comment.update.UpdateCommentUseCase;
import boo.ecrodrigues.user.domain.comment.CommentGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommentUseCaseConfig {

  private final CommentGateway commentGateway;

  public CommentUseCaseConfig(final CommentGateway commentGateway) {
    this.commentGateway = commentGateway;
  }

  @Bean
  public CreateCommentUseCase createCommentUseCase() {
    return new DefaultCreateCommentUseCase(commentGateway);
  }

  @Bean
  public UpdateCommentUseCase updateCommentUseCase() {
    return new DefaultUpdateCommentUseCase(commentGateway);
  }

  @Bean
  public ListCommentUseCase listCommentUseCase() {
    return new DefaultListCommentsUseCase(commentGateway);
  }
}
