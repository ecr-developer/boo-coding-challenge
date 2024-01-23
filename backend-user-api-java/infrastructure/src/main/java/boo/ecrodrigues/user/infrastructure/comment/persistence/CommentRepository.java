package boo.ecrodrigues.user.infrastructure.comment.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<CommentEntity, String> {
}
