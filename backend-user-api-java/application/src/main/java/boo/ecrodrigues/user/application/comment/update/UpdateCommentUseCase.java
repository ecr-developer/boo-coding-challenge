package boo.ecrodrigues.user.application.comment.update;

import boo.ecrodrigues.user.application.UseCase;

public sealed abstract class UpdateCommentUseCase
          extends UseCase<UpdateCommentCommand, UpdateCommentOutput>
          permits DefaultUpdateCommentUseCase {
}
