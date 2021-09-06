package io.realworld.backend.domain.service;

import io.realworld.backend.domain.aggregate.user.ConduitUser;
import java.util.Optional;

public interface AuthenticationService {
  /** Returns current authenticated user. */
  Optional<ConduitUser> getCurrentUser();

  /** Returns a JWT token used to authenticate current user. */
  Optional<String> getCurrentToken();

  /** Checks if provided credentials correct and returns relevant user. */
  Optional<ConduitUser> authenticate(String email, String password);

  /** Returns a hash of the password. */
  String encodePassword(String password);
}
