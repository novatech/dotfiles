package my.crud.api.security;

import my.crud.api.repository.UserRepository;
import my.crud.api.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Autowired
    public WebSecurityConfig(JwtService jwtService, UserRepository userRepository, ExceptionHandlerFilter exceptionHandlerFilter) {
        super();
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.exceptionHandlerFilter = exceptionHandlerFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .antMatchers(HttpMethod.POST, "/users/register", "/users/login").permitAll()
                .antMatchers("/notes/**", "/users/**").authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtService, userRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, CorsFilter.class);
    }
}