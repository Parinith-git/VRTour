package com.example.vrtourism.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
    }

    @Test
    void generateToken_shouldReturnNonNullToken() {
        String token = tokenService.generateToken(1L);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateToken_shouldReturnUniqueTokensForSameUser() {
        String token1 = tokenService.generateToken(1L);
        String token2 = tokenService.generateToken(1L);
        assertNotEquals(token1, token2);
    }

    @Test
    void getUserIdFromToken_shouldReturnCorrectUserId() {
        Long userId = 42L;
        String token = tokenService.generateToken(userId);
        Long result = tokenService.getUserIdFromToken(token);
        assertEquals(userId, result);
    }

    @Test
    void getUserIdFromToken_shouldReturnNullForInvalidToken() {
        Long result = tokenService.getUserIdFromToken("invalid-token-abc");
        assertNull(result);
    }

    @Test
    void invalidateToken_shouldRemoveToken() {
        String token = tokenService.generateToken(1L);
        assertNotNull(tokenService.getUserIdFromToken(token));

        tokenService.invalidateToken(token);
        assertNull(tokenService.getUserIdFromToken(token));
    }

    @Test
    void invalidateToken_shouldNotAffectOtherTokens() {
        String token1 = tokenService.generateToken(1L);
        String token2 = tokenService.generateToken(2L);

        tokenService.invalidateToken(token1);

        assertNull(tokenService.getUserIdFromToken(token1));
        assertEquals(2L, tokenService.getUserIdFromToken(token2));
    }
}
