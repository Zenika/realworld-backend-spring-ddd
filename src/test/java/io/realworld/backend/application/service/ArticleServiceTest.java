package io.realworld.backend.application.service;

import io.realworld.backend.domain.aggregate.user.UserRepository;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ArticleServiceTest {

    @Autowired
    private @MonotonicNonNull ArticleService articleService;

    @MockBean
    private @MonotonicNonNull UserRepository userRepositoryMock;

    @Nested
    class CreateArticleTest {
        @Test
        void dummy() {
            assertTrue(true);
            assertNotNull(articleService);
        }
    }

}
