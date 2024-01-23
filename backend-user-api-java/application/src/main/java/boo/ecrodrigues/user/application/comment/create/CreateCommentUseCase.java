package boo.ecrodrigues.user.application.comment.create;

import boo.ecrodrigues.user.application.UseCase;

public sealed abstract class CreateCommentUseCase
    extends UseCase<CreateCommentCommand, CreateCommentOutput>
    permits DefaultCreateCommentUseCase {
}
