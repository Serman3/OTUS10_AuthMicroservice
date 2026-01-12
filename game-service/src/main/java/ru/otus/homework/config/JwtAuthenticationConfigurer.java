package ru.otus.homework.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.otus.homework.security.JwtAuthenticationConverter;
import ru.otus.homework.security.token.Token;

import java.util.function.Function;

public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

    private Function<String, Token> accessTokenStringDeserializer;

    private Function<String, Token> refreshTokenStringDeserializer;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        var jwtVerifierFilter = new JwtVerifierFilter()
                .setJwtAuthenticationConverter(new JwtAuthenticationConverter(this.accessTokenStringDeserializer, this.refreshTokenStringDeserializer));

        builder
                .addFilterBefore(jwtVerifierFilter, UsernamePasswordAuthenticationFilter.class);
    }

    public JwtAuthenticationConfigurer accessTokenStringDeserializer(
            Function<String, Token> accessTokenStringDeserializer) {
        this.accessTokenStringDeserializer = accessTokenStringDeserializer;
        return this;
    }

    public JwtAuthenticationConfigurer refreshTokenStringDeserializer(
            Function<String, Token> refreshTokenStringDeserializer) {
        this.refreshTokenStringDeserializer = refreshTokenStringDeserializer;
        return this;
    }
}

