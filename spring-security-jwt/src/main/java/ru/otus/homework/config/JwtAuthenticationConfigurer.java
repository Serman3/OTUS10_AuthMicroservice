package ru.otus.homework.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.otus.homework.security.JwtAuthenticationConverter;
import ru.otus.homework.security.token.Token;
import ru.otus.homework.token.TokenAuthenticationUserDetailsService;
import ru.otus.homework.datasource.dao.GameDao;
import ru.otus.homework.datasource.dao.UserAuthDao;
import ru.otus.homework.filter.JwtLogoutFilter;
import ru.otus.homework.filter.RefreshTokenFilter;
import ru.otus.homework.filter.RequestGameJwtTokensFilter;
import ru.otus.homework.filter.RequestJwtTokensFilter;

import java.util.function.Function;

public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

    private Function<Token, String> refreshTokenStringSerializer = Object::toString;

    private Function<Token, String> accessTokenStringSerializer = Object::toString;

    private Function<String, Token> accessTokenStringDeserializer;

    private Function<String, Token> refreshTokenStringDeserializer;

    private UserAuthDao userAuthDao;

    private GameDao spaceBattleDao;

    @Override
    public void init(HttpSecurity builder) throws Exception {
        var csrfConfigurer = builder.getConfigurer(CsrfConfigurer.class);
        if (csrfConfigurer != null) {
            csrfConfigurer.ignoringRequestMatchers(new AntPathRequestMatcher("/jwt/tokens", "POST"));
        }
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        var requestGameJwtTokensFilter = new RequestGameJwtTokensFilter();
        requestGameJwtTokensFilter.setAccessTokenStringSerializer(this.accessTokenStringSerializer);
        requestGameJwtTokensFilter.setRefreshTokenStringSerializer(this.refreshTokenStringSerializer);
        requestGameJwtTokensFilter.setSpaceBattleDao(spaceBattleDao);

        var requestJwtTokensFilter = new RequestJwtTokensFilter();
        requestJwtTokensFilter.setAccessTokenStringSerializer(this.accessTokenStringSerializer);
        requestJwtTokensFilter.setRefreshTokenStringSerializer(this.refreshTokenStringSerializer);

        var jwtAuthenticationFilter = new AuthenticationFilter(builder.getSharedObject(AuthenticationManager.class),
                new JwtAuthenticationConverter(this.accessTokenStringDeserializer, this.refreshTokenStringDeserializer));
        jwtAuthenticationFilter
                .setSuccessHandler((request, response, authentication) -> CsrfFilter.skipRequest(request));
        jwtAuthenticationFilter
                .setFailureHandler((request, response, exception) -> response.sendError(HttpServletResponse.SC_FORBIDDEN));

        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(
                new TokenAuthenticationUserDetailsService(this.userAuthDao));

        var refreshTokenFilter = new RefreshTokenFilter();
        refreshTokenFilter.setAccessTokenStringSerializer(this.accessTokenStringSerializer);

        var jwtLogoutFilter = new JwtLogoutFilter(this.userAuthDao);

        builder
                .addFilterAfter(requestJwtTokensFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(requestGameJwtTokensFilter, RequestJwtTokensFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, CsrfFilter.class)
                .addFilterAfter(refreshTokenFilter, ExceptionTranslationFilter.class)
                .addFilterAfter(jwtLogoutFilter, ExceptionTranslationFilter.class)
                .authenticationProvider(authenticationProvider);
    }

    public JwtAuthenticationConfigurer refreshTokenStringSerializer(
            Function<Token, String> refreshTokenStringSerializer) {
        this.refreshTokenStringSerializer = refreshTokenStringSerializer;
        return this;
    }

    public JwtAuthenticationConfigurer accessTokenStringSerializer(
            Function<Token, String> accessTokenStringSerializer) {
        this.accessTokenStringSerializer = accessTokenStringSerializer;
        return this;
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

    public JwtAuthenticationConfigurer userAuthDao(UserAuthDao userAuthDao) {
        this.userAuthDao = userAuthDao;
        return this;
    }

    public JwtAuthenticationConfigurer spaceBattleDao(GameDao spaceBattleDao) {
        this.spaceBattleDao = spaceBattleDao;
        return this;
    }
}

