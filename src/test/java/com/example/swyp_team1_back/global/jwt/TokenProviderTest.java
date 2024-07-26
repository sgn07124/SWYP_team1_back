package com.example.swyp_team1_back.global.jwt;

import com.example.swyp_team1_back.domain.user.dto.TokenDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TokenProviderTest {

    private static final String SECRET_KEY = "UlYth+E49WB6dlTGdGG2Ll0c8PBcFjgbqt+IOP/Z6F7MlpWB04rsL9iIc9GGlnWVFV1PmWqmp9tDkox4kJHz1Q==";
    private static final String AUTHORITIES_KEY = "auth";

    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new TokenProvider(SECRET_KEY);
    }

    @Test
    void testGenerateTokenDto() {
        Authentication authentication = Mockito.mock(Authentication.class);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);

        Mockito.when(authentication.getName()).thenReturn("testUser");
        Mockito.when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

        TokenDto token = tokenProvider.generateTokenDto(authentication, id, email, name);

        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
        assertEquals("Bearer", token.getGrantType());
        assertTrue(token.getAccessTokenExpiresIn() > new Date().getTime());
    }

    @Test
    void testGetAuthentication() {
        Authentication authentication = Mockito.mock(Authentication.class);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);

        Mockito.when(authentication.getName()).thenReturn("testUser");
        Mockito.when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

        TokenDto token = tokenProvider.generateTokenDto(authentication, id, email, name);
        Authentication auth = tokenProvider.getAuthentication(token.getAccessToken());

        assertNotNull(auth);
        assertEquals("testUser", auth.getName());
        assertEquals(1, auth.getAuthorities().size());
        assertEquals("ROLE_USER", auth.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void testValidateToken() {
        Authentication authentication = Mockito.mock(Authentication.class);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);

        Mockito.when(authentication.getName()).thenReturn("testUser");
        Mockito.when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

        TokenDto token = tokenProvider.generateTokenDto(authentication, id, email, name);
        boolean isValid = tokenProvider.validateToken(token.getAccessToken());

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidToken() {
        String invalidToken = "invalid.token.value";
        boolean isValid = tokenProvider.validateToken(invalidToken);

        assertFalse(isValid);
    }
}
