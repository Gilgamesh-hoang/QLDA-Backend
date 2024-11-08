package com.commic.testLogin;
import com.commic.v1.api.user.AuthenticationController;
import com.commic.v1.dto.requests.AuthenticationRequest;
import com.commic.v1.dto.requests.LogoutRequest;
import com.commic.v1.dto.responses.JwtResponse;
import com.commic.v1.services.authentication.IAuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class testLoginController {
    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private IAuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateSuccess() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("bsokqopppp", "Ducvui2003*");
        JwtResponse jwtResponse = new JwtResponse("token", System.currentTimeMillis() + 3600000);
        when(authenticationService.login(request)).thenReturn(jwtResponse);

        // Act
        ResponseEntity<JwtResponse> response = authenticationController.authenticate(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(jwtResponse, response.getBody());
        verify(authenticationService).login(request);
    }

    @Test
    void testLogoutSuccess() {
        // Arrange
        LogoutRequest request = new LogoutRequest("token");

        // Act
        ResponseEntity<Void> response = authenticationController.logout(request);

        // Assert
        assertEquals(204, response.getStatusCodeValue());
        verify(authenticationService).logout(request);
    }
}


