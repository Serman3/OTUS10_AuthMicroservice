package ru.otus.homework.security.refresh;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.security.token.Token;

import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;

public class RefreshTokenJweStringDeserializer implements Function<String, Token> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenJweStringDeserializer.class);

    private final JWEDecrypter jweDecrypter;

    public RefreshTokenJweStringDeserializer(JWEDecrypter jweDecrypter) {
        this.jweDecrypter = jweDecrypter;
    }

    @Override
    public Token apply(String string) {
        try {
            var encryptedJWT = EncryptedJWT.parse(string);
            encryptedJWT.decrypt(this.jweDecrypter);
            var claimsSet = encryptedJWT.getJWTClaimsSet();
            return new Token(
                    UUID.fromString(claimsSet.getJWTID()),
                    claimsSet.getSubject(),
                    claimsSet.getStringClaim("gameId"),
                    claimsSet.getStringListClaim("authorities"),
                    claimsSet.getIssueTime().toInstant(),
                    claimsSet.getExpirationTime().toInstant());
        } catch (ParseException | JOSEException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }

        return null;
    }

}
