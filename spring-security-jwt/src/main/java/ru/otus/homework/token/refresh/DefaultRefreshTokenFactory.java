package ru.otus.homework.token.refresh;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.otus.homework.security.token.Token;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

public class DefaultRefreshTokenFactory implements BiFunction<Authentication, HttpServletRequest, Token> {

    private Duration tokenTtl = Duration.ofDays(1); // Срок жизни рефреш токена по умолчанию равен 1-му дню

    @Override
    public Token apply(Authentication authentication, HttpServletRequest httpServletRequest) {
        List<String> authorities = new LinkedList<>();
        authorities.add("JWT_REFRESH");
        authorities.add("JWT_LOGOUT");

        authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> "GRANT_" + authority)
                .forEach(authorities::add);

        Instant now = Instant.now();
        return new Token(UUID.randomUUID(), authentication.getName(), httpServletRequest.getHeader("gameId"), authorities, now, now.plus(tokenTtl));
    }

    public void setTokenTtl(Duration tokenTtl) {
        this.tokenTtl = tokenTtl;
    }
}
