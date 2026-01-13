package com.training.some_social_network.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.training.some_social_network.exceptions.NotValidDataException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    @Value("${jwt.token.secretKey:some_key}")
    private String secretKey;
    @Value("${jwt.token.expirationMinute:360000}")
    private long expirationMinute;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    private Algorithm algorithm;

    public String getAccessToken(String userId) {
        Date now = new Date();
        Date expireAt = new Date(now.getTime() + expirationMinute * 60 * 1000);
        return createAccessToken(userId, now, expireAt);
    }

    private String createAccessToken(String userId, Date now, Date expireAt) {
        String issuerUri = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        return JWT.create()
                .withIssuer(issuerUri)
                .withSubject(userId)
                .withIssuedAt(now)
                .withExpiresAt(expireAt)
                .sign(algorithm)
                .strip();
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token) {
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        return jwtVerifier.verify(token);
    }

    public String resolveToken(HttpServletRequest request) {
        return resolveToken(request.getHeader(HEADER_STRING));
    }

    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public Boolean validateToken(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        NotValidDataException.throwIf(decodedJWT.getExpiresAt().before(new Date()), "Токен не действителен");
        return true;
    }

    @PostConstruct
    protected void init() {
        algorithm = Algorithm.HMAC512(secretKey);
    }
}
