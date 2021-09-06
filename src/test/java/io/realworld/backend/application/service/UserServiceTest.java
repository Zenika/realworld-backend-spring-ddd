package io.realworld.backend.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.realworld.backend.application.exception.InvalidPasswordException;
import io.realworld.backend.domain.aggregate.user.ConduitUser;
import io.realworld.backend.domain.aggregate.user.UserRepository;
import io.realworld.backend.domain.service.JwtService;
import io.realworld.backend.rest.api.LoginUserData;
import io.realworld.backend.rest.api.LoginUserRequestData;
import io.realworld.backend.rest.api.UserResponseData;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

  @Autowired private UserService userService;

  @MockBean private PasswordEncoder passwordEncoderMock;

  @MockBean private UserRepository userRepositoryMock;

  @MockBean private JwtService jwtService;

  @Test
  void correct_user_info_should_login_successfully() {
    // Given
    final ConduitUser fakeUser =
        new ConduitUser("training@zenika.com", "TrainingZenika", "fakeHash");
    Mockito.when(userRepositoryMock.findByEmail("training@zenika.com"))
        .thenReturn(Optional.of(fakeUser));
    Mockito.when(passwordEncoderMock.matches("toto", "fakeHash")).thenReturn(true);
    Mockito.when(jwtService.generateToken(fakeUser)).thenReturn("fakeToken");

    // When
    final ResponseEntity<UserResponseData> response =
        userService.login(
            new LoginUserRequestData()
                .user(new LoginUserData().email("training@zenika.com").password("toto")));

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("TrainingZenika", response.getBody().getUser().getUsername());
    assertEquals("fakeToken", response.getBody().getUser().getToken());
  }

  @Test
  void bad_password_should_answer_unauthorized() {
    // Given
    final ConduitUser fakeUser =
        new ConduitUser("training@zenika.com", "TrainingZenika", "fakeHash");
    Mockito.when(userRepositoryMock.findByEmail("training@zenika.com"))
        .thenReturn(Optional.of(fakeUser));
    Mockito.when(passwordEncoderMock.matches("toto", "fakeHash")).thenReturn(false);

    // When / Then
    assertThrows(
        InvalidPasswordException.class,
        () ->
            userService.login(
                new LoginUserRequestData()
                    .user(new LoginUserData().email("training@zenika.com").password("toto"))));
  }
}
