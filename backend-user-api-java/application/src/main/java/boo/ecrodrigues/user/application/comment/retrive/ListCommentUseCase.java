package boo.ecrodrigues.user.application.comment.retrive;

import boo.ecrodrigues.user.application.MultiUseCase;
import boo.ecrodrigues.user.domain.pagination.Fields;
import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;

public sealed abstract class ListCommentUseCase
    extends MultiUseCase<SearchQuery, Fields, Pagination<CommentListOutput>>
    permits DefaultListCommentsUseCase {

}
