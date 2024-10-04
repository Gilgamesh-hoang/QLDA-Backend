package com.commic.v1.services.authentication;

import com.commic.v1.dto.requests.AuthenticationRequest;
import com.commic.v1.dto.requests.LogoutRequest;
import com.commic.v1.dto.responses.JwtResponse;

public interface IAuthenticationService {
    JwtResponse login(AuthenticationRequest request);

    void logout(LogoutRequest request);
}
