package com.github.remusselea.scentdb.security;

import com.github.remusselea.scentdb.config.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenProvider {

    private final AppConfig appConfig;

    public TokenProvider(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "scentdb-api";
    public static final String TOKEN_AUDIENCE = "scentdb-app";

    public String generate(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        List<String> roles = user.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        byte[] signingKey = appConfig.getJwtConfig().getSecret().getBytes();

        return Jwts.builder()
            .setHeaderParam("typ", TOKEN_TYPE)
            .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
            .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(appConfig.getJwtConfig().getExpirationMinutes()).toInstant()))
            .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
            .setId(UUID.randomUUID().toString())
            .setIssuer(TOKEN_ISSUER)
            .setAudience(TOKEN_AUDIENCE)
            .setSubject(user.getUsername())
            .claim("rol", roles)
            .claim("name", user.getName())
            .claim("preferred_username", user.getUsername())
            .claim("email", user.getEmail())
            .compact();
    }

    public Optional<Jws<Claims>> validateTokenAndGetJws(String token) {
        try {
            byte[] signingKey = appConfig.getJwtConfig().getSecret().getBytes();

            Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);

            return Optional.of(jws);
        } catch (ExpiredJwtException exception) {
            log.error("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.error("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.error("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
        } catch (SecurityException exception) {
            log.error("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.error("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
        }
        return Optional.empty();
    }

}
