package my.crud.api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static my.crud.api.Constants.API_KEY;
import static my.crud.api.Constants.TOKEN_EXPIRY;

@Service
public class JwtService {

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(TOKEN_EXPIRY)))
                .signWith(API_KEY)
                .compact();
    }

    public Optional<String> getSubjectFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(API_KEY)
                .build()
                .parseClaimsJws(token).getBody();
        return Optional.ofNullable(claims.getSubject());
    }
}