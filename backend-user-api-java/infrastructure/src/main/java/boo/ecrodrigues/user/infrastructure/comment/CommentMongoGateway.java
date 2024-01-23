package boo.ecrodrigues.user.infrastructure.comment;

import boo.ecrodrigues.user.domain.comment.Comment;
import boo.ecrodrigues.user.domain.comment.CommentGateway;
import boo.ecrodrigues.user.domain.comment.CommentID;
import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.domain.pagination.SearchQuery;
import boo.ecrodrigues.user.infrastructure.comment.persistence.CommentEntity;
import boo.ecrodrigues.user.infrastructure.comment.persistence.CommentRepository;
import boo.ecrodrigues.user.infrastructure.configuration.DatabaseCollectionsConfig;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
  public Pagination<Comment> findAll(SearchQuery aQuery) {
    final String collectionName = collectionsConfig.getComment();
    // Pagination
    final var page = PageRequest.of(
        aQuery.page(),
        aQuery.perPage(),
        Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
    );

    final Query query = new Query().addCriteria(
        Criteria.where("title").ne(null)
    );

    final List<CommentEntity> comments = mongoTemplate.find(query, CommentEntity.class, collectionName);

    final var pageResult = PageableExecutionUtils.getPage(comments, page, () -> this.mongoTemplate.count(Query
        .of(query)
        .limit(-1)
        .skip(-1), collectionName));

    return new Pagination<>(
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        comments.stream().map(CommentEntity::toAggregate).toList());
  }

  public Page<CommentEntity> findAllCommentBy(
      final Set<MBTI> mdtis,
      final Set<Enneagram> enneagrams,
      final Set<Zodiac> zodiacs,
      final Pageable pageable
  ) {
    final String collectionName = collectionsConfig.getComment();
    final Query commentQuery = new Query();

    if (!CollectionUtils.isEmpty(mdtis)) {
      commentQuery.addCriteria(Criteria
          .where("mbti")
          .in(mdtis));
    }

    if (!CollectionUtils.isEmpty(enneagrams)) {
      commentQuery.addCriteria(Criteria
          .where("enneagram")
          .in(enneagrams));
    }

    if (!CollectionUtils.isEmpty(zodiacs)) {
      commentQuery.addCriteria(Criteria
          .where("zodiac")
          .in(zodiacs));
    }

    final var comments = this.mongoTemplate.find(commentQuery, CommentEntity.class, collectionName);

    return PageableExecutionUtils.getPage(comments, pageable, () -> this.mongoTemplate.count(Query
        .of(commentQuery)
        .limit(-1)
        .skip(-1), collectionName));
  }
}
