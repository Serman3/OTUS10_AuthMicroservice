package ru.otus.homework.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import ru.otus.homework.datasource.dao.SpaceBattleDao;
import ru.otus.homework.datasource.dao.UserAuthDao;
import ru.otus.homework.security.access.AccessTokenJwsStringDeserializer;
import ru.otus.homework.token.access.AccessTokenJwsStringSerializer;
import ru.otus.homework.security.refresh.RefreshTokenJweStringDeserializer;
import ru.otus.homework.token.refresh.RefreshTokenJweStringSerializer;
import java.text.ParseException;

@Configuration
public class SecurityConfig {

    private final UserAuthDao userAuthDao;
    private final SpaceBattleDao spaceBattleDao;

    @Autowired
    public SecurityConfig(UserAuthDao userAuthDao,
                          SpaceBattleDao spaceBattleDao) {
        this.userAuthDao = userAuthDao;
        this.spaceBattleDao = spaceBattleDao;
    }

    @Bean
    public JwtAuthenticationConfigurer jwtAuthenticationConfigurer(
            @Value("${jwt.access-token-key}") String accessTokenKey,
            @Value("${jwt.refresh-token-key}") String refreshTokenKey,
            JdbcTemplate jdbcTemplate
    ) throws ParseException, JOSEException {
        return new JwtAuthenticationConfigurer()
                .accessTokenStringSerializer(new AccessTokenJwsStringSerializer(
                        new MACSigner(OctetSequenceKey.parse(accessTokenKey))
                ))
                .refreshTokenStringSerializer(new RefreshTokenJweStringSerializer(
                        new DirectEncrypter(OctetSequenceKey.parse(refreshTokenKey))
                ))
                .accessTokenStringDeserializer(new AccessTokenJwsStringDeserializer(
                        new MACVerifier(OctetSequenceKey.parse(accessTokenKey))
                ))
                .refreshTokenStringDeserializer(new RefreshTokenJweStringDeserializer(
                        new DirectDecrypter(OctetSequenceKey.parse(refreshTokenKey))
                ))
                .userAuthDao(userAuthDao)
                .spaceBattleDao(spaceBattleDao);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationConfigurer jwtAuthenticationConfigurer) throws Exception {
        http.with(jwtAuthenticationConfigurer, Customizer.withDefaults());

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/auth/**", "/jwt/**").permitAll()
                                .requestMatchers("/swagger-ui/**", "/swagger-resources/*", "/v3/api-docs/**").permitAll()
                                .anyRequest().hasAnyRole("USER", "MANAGER"))
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userAuthDao.findUserByUsername(username)
                .map(userDto -> {
                    return User.builder()
                            .username(userDto.getUsername())
                            .password(userDto.getPassword())
                            .authorities(userAuthDao.findAllUserRolesByUserId(userDto.getId())
                                    .stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .toList()
                            ).build();
                })
                .stream()
                .findFirst()
                .orElse(null);
    }

}
