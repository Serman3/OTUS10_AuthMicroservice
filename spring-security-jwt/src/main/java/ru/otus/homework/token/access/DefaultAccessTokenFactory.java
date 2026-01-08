package ru.otus.homework.token.access;

import ru.otus.homework.Token;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;

public class DefaultAccessTokenFactory implements Function<Token, Token> {

    private Duration tokenTtl = Duration.ofMinutes(5); // Срок жизни access токена по умолчанию равен 5-ти дням


    //На вход принимается рефреш токен
    @Override
    public Token apply(Token token) {
        Instant now = Instant.now();
        return new Token(token.id(), token.subject(),
                token.authorities().stream()
                        .filter(authority -> authority.startsWith("GRANT_"))
                        .map(authority -> authority.replace("GRANT_", ""))
                        .toList(), now, now.plus(tokenTtl));
    }

    public void setTokenTtl(Duration tokenTtl) {
        this.tokenTtl = tokenTtl;
    }

}
