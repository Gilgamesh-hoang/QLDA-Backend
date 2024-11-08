package com.commic.testLogin;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.commic.v1.dto.requests.AuthenticationRequest;
import com.commic.v1.dto.requests.LogoutRequest;
import com.commic.v1.dto.responses.JwtResponse;
import com.commic.v1.entities.InvalidatedToken;
import com.commic.v1.exception.AppException;
import com.commic.v1.exception.ErrorCode;
import com.commic.v1.jwt.JwtTokenUtil;
import com.commic.v1.repositories.InvalidatedTokenRepository;
import com.commic.v1.services.authentication.AuthenticationService;
import com.commic.v1.services.user.UserDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;

public class testLoginService {
    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private InvalidatedTokenRepository invalidatedTokenRepository;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserDetailServiceImpl userDetailsService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_Success() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("bsokqopppp", "Ducvui2003*");
        UserDetails user = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("bsokqopppp")).thenReturn(user);

        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(jwtTokenUtil.generateToken(user)).thenReturn("testJwtToken");

        // Act
        JwtResponse response = authenticationService.login(request);

        // Assert
        assertNotNull(response.getToken());
    }

    @Test
    public void testLogin_UserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest("nonexistentUser", "password");
        when(userDetailsService.loadUserByUsername("nonexistentUser"))
                .thenThrow(new UsernameNotFoundException("User not found"));

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authenticationService.login(request));
    }

    @Test
    public void testLogout_Success() {
        // Arrange
        String token = "sampleToken";
        LogoutRequest request = new LogoutRequest(token);
        when(jwtTokenUtil.extractJwtID(token)).thenReturn("jwtId");
        when(jwtTokenUtil.extractExpriration(token)).thenReturn(new Date(System.currentTimeMillis() + 1000));

        // Act
        authenticationService.logout(request);

        // Assert
        verify(invalidatedTokenRepository, times(1)).save(any(InvalidatedToken.class));
    }


}
