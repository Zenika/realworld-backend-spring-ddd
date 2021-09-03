package io.realworld.backend.infrastructure.security;

import io.realworld.backend.domain.aggregate.user.User;
import io.realworld.backend.domain.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtTokenFilterTest {
    @Mock
    private JwtService jwtServiceMock;
    @Mock
    private FilterChain mockFilterChain;

    @Test
    void authSuccess() throws ServletException, IOException {
        final JwtTokenFilter filter = new JwtTokenFilter(jwtServiceMock);

        when(jwtServiceMock.getUser("fake-auth-token"))
            .thenReturn(Optional.of(new User("fake-user@caramail.fr", "OldGuy", "fakePasswordHash")));

        final MockHttpServletRequest fakeRequest = new MockHttpServletRequest("GET", "/user");
        fakeRequest.addHeader("Authorization", "Bearer fake-auth-token");

        final MockHttpServletResponse fakeResponse = new MockHttpServletResponse();
        filter.doFilterInternal(fakeRequest, fakeResponse, mockFilterChain);

        verify(mockFilterChain).doFilter(fakeRequest, fakeResponse);

        final UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assertEquals("fake-user@caramail.fr", ud.getUsername());
    }
}
