package ru.otus.homework.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.otus.homework.security.token.Token;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/**", HttpMethod.POST.name());

    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.requestMatcher.matches(request)) {
            if (this.securityContextRepository.containsContext(request)) {
                var context = this.securityContextRepository.loadDeferredContext(request).get();
                if (context != null && context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken) {
                    Token token = (Token) context.getAuthentication().getPrincipal();
                    List<SimpleGrantedAuthority> simpleGrantedAuthorities = token.authorities().stream().map(SimpleGrantedAuthority::new).toList();
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            User.builder()
                                    .username(token.subject())
                                    .password("nopassword")
                                    .accountExpired(false)
                                    .accountLocked(false)
                                    .disabled(false)
                                    .credentialsExpired(token.expiresAt().isAfter(Instant.now()))
                                    .authorities(simpleGrantedAuthorities)
                                    .build(),
                            "nopassword",
                            simpleGrantedAuthorities
                    );
                    if (SecurityContextHolder.getContext().getAuthentication() == null)
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            throw new AccessDeniedException("User must be authenticated with JWT");
        }
        filterChain.doFilter(request, response);
    }
}
