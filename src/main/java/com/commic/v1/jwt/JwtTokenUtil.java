package com.commic.v1.jwt;


import com.commic.v1.repositories.InvalidatedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Key;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/*
The JwtTokenUtil is responsible for performing JWT operations like creation and validation.
It makes use of the io.jsonwebtoken.Jwts for achieving this.
 */

@Component
public class JwtTokenUtil implements Serializable {
    @Autowired
    private InvalidatedTokenRepository tokenRepository;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;


    // generate token for user
    public String generateToken(UserDetails userDetails) {
        if (userDetails == null)
            return null;

        Map<String, Object> claims = extractUserRoles(userDetails);
        return createToken(claims, userDetails.getUsername());
    }

    private Map<String, Object> extractUserRoles(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails != null) {
            boolean isAdmin = false;
            boolean isUser = false;
            for (GrantedAuthority authority : userDetails.getAuthorities()) {
                if (authority.getAuthority().equals("ADMIN")) {
                    isAdmin = true;
                } else if (authority.getAuthority().equals("USER")) {
                    isUser = true;
                }
            }
            claims.put("isAdmin", isAdmin);
            claims.put("isUser", isUser);
        }
        return claims;
    }

    // while creating the token -
    // 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
    // 2. Sign the JWT using the HS512 algorithm and secret key.
    // 3. According to JWS Compact
    // Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    // compaction of the JWT to a URL-safe string
    private String createToken(Map<String, Object> claims, String username) {
        claims.put("jwtID", UUID.randomUUID().toString()); // Đặt ID vào payload
        return Jwts.builder().setHeaderParam("type", "JWT").setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256).compact();
    }

    private Key getSigningKey(String key) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // trích xuất thông tin
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey(SECRET_KEY)).parseClaimsJws(token).getBody();
    }

    // trích xuất thông tin cho 1 claim
    private <T> T extractClaims(String token, Function<Claims, T> claimsFuntion) {
        final Claims claims = extractAllClaims(token);
        return claimsFuntion.apply(claims);
    }

    public String extractJwtID(String token) {
        return extractClaims(token, claims -> claims.get("jwtID", String.class));
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    // lấy ra thời gian hết hạn
    public Date extractExpriration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpriration(token).before(new Timestamp(System.currentTimeMillis()));
    }

    private boolean isExists(String token) {
        return tokenRepository.existsById(extractJwtID(token));
    }

    // kiem tra tinh hop le
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token) && !isExists(token);
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token) && !isExists(token);
    }

    public static void main(String[] args) {
        long a = System.currentTimeMillis() + JwtTokenUtil.JWT_TOKEN_VALIDITY;
        System.out.println(a);
        Timestamp timestamp = new Timestamp(a);
        System.out.println(timestamp);
        System.out.println(timestamp.getTime());
    }
}
