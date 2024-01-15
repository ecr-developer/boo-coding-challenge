package boo.ecrodrigues.user.infrastructure.account.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountJpaEntity, String> {

}
