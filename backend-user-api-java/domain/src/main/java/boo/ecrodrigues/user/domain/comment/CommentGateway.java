package boo.ecrodrigues.user.domain.comment;

import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;
import java.util.Set;

public interface CommentGateway {

  Comment create(Comment aComment);

  Comment update(Comment aComment);

  Pagination<Comment> findAll(Set<String> commentIds, Set<String> existingFields, SearchQuery aQuery);

}
