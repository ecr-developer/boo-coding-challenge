package boo.ecrodrigues.user.domain.comment;

import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;
import java.util.Optional;
import java.util.Set;

public interface CommentGateway {

  Comment create(Comment aComment);

  Comment update(Comment aComment);

  Optional<Comment> findById(CommentID anId);

  Pagination<Comment> findAll(SearchQuery aQuery);

}
