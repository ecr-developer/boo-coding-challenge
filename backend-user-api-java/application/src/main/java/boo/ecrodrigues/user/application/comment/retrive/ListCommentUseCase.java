package boo.ecrodrigues.user.application.comment.retrive;

import boo.ecrodrigues.user.application.UseCase;
import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;

public sealed abstract class ListCommentUseCase
    extends UseCase<SearchQuery, Pagination<CommentListOutput>>
    permits DefaultListCommentsUseCase {

}
