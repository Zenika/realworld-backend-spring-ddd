package io.realworld.backend.domain.aggregate.user;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<ConduitUser, Long> {
  Optional<ConduitUser> findByEmail(String username);

  Optional<ConduitUser> findByUsername(String username);
}
