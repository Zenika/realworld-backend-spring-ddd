package io.realworld.backend.domain.service;

import io.realworld.backend.domain.aggregate.user.ConduitUser;
import java.util.Optional;

public interface JwtService {
  /** Generates JWT token for a given user. */
  String generateToken(ConduitUser user);

  /** Finds a user that given token was generated for. */
  Optional<ConduitUser> getUser(String token);
}
