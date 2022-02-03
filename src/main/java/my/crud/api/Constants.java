package my.crud.api;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Duration;

public class Constants {
    private Constants() {
        throw new IllegalStateException("constants");
    }

    public static final Key API_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public static final Duration TOKEN_EXPIRY = Duration.ofMinutes(5);

    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String API_VERSION = "v1";
    public static final String API_DESCRIPTION = "Simple CRUD API";
}
