package boo.ecrodrigues.user.infrastructure.account.persistence;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<AccountJpaEntity, String> {

  Page<AccountJpaEntity> findAll(Specification<AccountJpaEntity> whereClause, Pageable page);

  @Query(value = "select c.id from Account c where c.id in :ids")
  List<String> existsByIds(@Param("ids") List<String> ids);

}
