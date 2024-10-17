package com.commic.v1.services.authentication;

import com.commic.v1.dto.requests.AuthenticationRequest;
import com.commic.v1.dto.requests.LogoutRequest;
import com.commic.v1.dto.responses.JwtResponse;
import com.commic.v1.entities.InvalidatedToken;
import com.commic.v1.exception.AppException;
import com.commic.v1.exception.ErrorCode;
import com.commic.v1.jwt.JwtTokenUtil;
import com.commic.v1.repositories.InvalidatedTokenRepository;
import com.commic.v1.services.user.UserDetailServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService implements IAuthenticationService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    InvalidatedTokenRepository invalidatedTokenRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserDetailServiceImpl userDetailsService;

    @Override
    public JwtResponse login(AuthenticationRequest request) {
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        Authentication authentication = authenticate(request.getUsername(), request.getPassword());
        boolean isAuthenticated = authentication.isAuthenticated();
        String token = isAuthenticated ? jwtTokenUtil.generateToken(user) : null;

        return new JwtResponse(token, System.currentTimeMillis() + JwtTokenUtil.JWT_TOKEN_VALIDITY);
    }

    private Authentication authenticate(String username, String password) {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException | BadCredentialsException e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
    public void logout(LogoutRequest request) {

        extractJwtIDAndSaveToken(request.getToken());
        SecurityContextHolder.clearContext();
    }

    private void extractJwtIDAndSaveToken(String token) {
        String jwtID = jwtTokenUtil.extractJwtID(token);
        Date expired = jwtTokenUtil.extractExpriration(token);

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .tokenId(jwtID)
                .expired(expired)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

}
