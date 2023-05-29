package io.realworld.backend.application.service;

import static org.junit.jupiter.api.Assertions.*;

import io.realworld.backend.domain.aggregate.user.ConduitUser;
import io.realworld.backend.domain.aggregate.user.UserRepository;
import io.realworld.backend.domain.service.AuthenticationService;
import io.realworld.backend.rest.api.MultipleArticlesResponseData;
import io.realworld.backend.rest.api.NewArticleData;
import io.realworld.backend.rest.api.NewArticleRequestData;
import io.realworld.backend.rest.api.SingleArticleResponseData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class ArticleServiceTest {

  @Autowired private ArticleService articleService;
  @Autowired private UserRepository userRepository;

  @MockBean private AuthenticationService authenticationService;

  @BeforeEach
  void setup() {
      final ConduitUser userForTests = userRepository.save(new ConduitUser("testing@training.com", "TestingTraining", "fakeHash"));
      Mockito.when(authenticationService.getCurrentUser()).thenReturn(Optional.of(userForTests));

      // cleanup to have independant tests
    final ResponseEntity<MultipleArticlesResponseData> allArticles = articleService.getArticles(null, null, null, 50, 0);
    allArticles.getBody().getArticles().forEach(a -> {
      articleService.deleteArticle(a.getSlug());
    });
  }

  @Nested
  class CreateArticleTest {
    @Test
    void nominal_case() {
        final ResponseEntity<SingleArticleResponseData> creationResponse = articleService.createArticle(new NewArticleRequestData().article(new NewArticleData().body("Toto").title("Test").description("Description").tagList(Collections.emptyList())));
        final String newArticleSlug = creationResponse.getBody().getArticle().getSlug();
        final ResponseEntity<SingleArticleResponseData> createdArticleResponse = articleService.getArticle(newArticleSlug);

        assertEquals("Test", createdArticleResponse.getBody().getArticle().getTitle());
        assertEquals("Toto", createdArticleResponse.getBody().getArticle().getBody());
        assertEquals("TestingTraining", createdArticleResponse.getBody().getArticle().getAuthor().getUsername());
    }

    @Test
    void get_case() {
        final ResponseEntity<MultipleArticlesResponseData> allArticles = articleService.getArticles(null, null, null, 50, 0);
        assertEquals(0, allArticles.getBody().getArticles().size());
    }
  }
}
