package boo.ecrodrigues.user.infrastructure.account.persistence;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<AccountEntity, String> {

  @Query(value = "{ accountId: { $in: [ :ids ] } }")
  List<String> existsByIds(@Param("ids") List<String> ids);
}
