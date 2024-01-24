package boo.ecrodrigues.user.infrastructure.comment;

import boo.ecrodrigues.user.domain.comment.Comment;
import boo.ecrodrigues.user.domain.comment.CommentGateway;
import boo.ecrodrigues.user.domain.comment.CommentID;
import boo.ecrodrigues.user.domain.pagination.Fields;
import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;
import boo.ecrodrigues.user.infrastructure.comment.persistence.CommentEntity;
import boo.ecrodrigues.user.infrastructure.comment.persistence.CommentRepository;
import boo.ecrodrigues.user.infrastructure.configuration.DatabaseCollectionsConfig;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

@Component
public class CommentMongoGateway implements CommentGateway {

  private static final Logger logger = LoggerFactory.getLogger(CommentMongoGateway.class);

  private final MongoTemplate mongoTemplate;

  private final DatabaseCollectionsConfig collectionsConfig;

  private final CommentRepository commentRepository;

  @Autowired
  public CommentMongoGateway(
      final MongoTemplate mongoTemplate,
      final DatabaseCollectionsConfig collectionsConfig,
      final CommentRepository commentRepository
  ) {
    this.mongoTemplate = mongoTemplate;
    this.collectionsConfig = collectionsConfig;
    this.commentRepository = commentRepository;
  }

  @Override
  public Comment create(Comment aComment) {
    final String collectionName = collectionsConfig.getComment();
    return mongoTemplate.insert(CommentEntity.from(aComment), collectionName).toAggregate();
  }

  @Override
  public Comment update(Comment aComment) {
    final String collectionName = collectionsConfig.getComment();
    return mongoTemplate.save(CommentEntity.from(aComment), collectionName).toAggregate();
  }

  @Override
  public Optional<Comment> findById(CommentID anId) {
    return this.commentRepository.findById(anId.getValue())
        .map(CommentEntity::toAggregate);
  }

  @Override
  public Pagination<Comment> findAll(SearchQuery aQuery, Fields fields) {
    final String collectionName = collectionsConfig.getComment();
    // Pagination
    final var page = PageRequest.of(
        aQuery.page(),
        aQuery.perPage(),
        Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
    );

    final Query commentQuery = new Query();
    commentQuery.addCriteria(
        Criteria.where("comment").ne(null)
    );

    if (fields != null) {
      if (fields.mbti() != null && StringUtils.isNotBlank(fields.mbti())) {
        commentQuery.addCriteria(Criteria
            .where("mbti")
            .in(fields.mbti()));
      }

      if (fields.enneagram() != null && StringUtils.isNotBlank(fields.enneagram())) {
        commentQuery.addCriteria(Criteria
            .where("enneagram")
            .in(fields.enneagram()));
      }

      if (fields.zodiac() != null && StringUtils.isNotBlank(fields.zodiac())) {
        commentQuery.addCriteria(Criteria
            .where("zodiac")
            .in(fields.zodiac()));
      }
    }

    final List<CommentEntity> comments = mongoTemplate.find(commentQuery, CommentEntity.class, collectionName);

    final var pageResult = PageableExecutionUtils.getPage(comments, page, () -> this.mongoTemplate.count(Query
        .of(commentQuery)
        .limit(-1)
        .skip(-1), collectionName));

    return new Pagination<>(
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        comments.stream().map(CommentEntity::toAggregate).toList());
  }
}
