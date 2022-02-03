package my.crud.api.security;

import my.crud.api.repository.UserRepository;
import my.crud.api.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static my.crud.api.Constants.HEADER_STRING;
import static my.crud.api.Constants.TOKEN_PREFIX;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtTokenFilter(JwtService jwtService, UserRepository userRepository) {
        super();
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        getTokenString(request.getHeader(HEADER_STRING)).flatMap(jwtService::getSubjectFromToken).ifPresent(email -> {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                userRepository.findByEmail(email).ifPresent(user -> {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                });
            }
        });
        filterChain.doFilter(request, response);
    }


    private Optional<String> getTokenString(String header) {
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            return Optional.empty();
        }
        return Optional.of(header.replace(TOKEN_PREFIX, "").trim());
    }
}

