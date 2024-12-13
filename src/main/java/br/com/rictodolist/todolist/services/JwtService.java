package br.com.rictodolist.todolist.services;

import br.com.rictodolist.todolist.constants.SecurityConstants;
import br.com.rictodolist.todolist.models.UserModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateAccessToken(UserModel user) {
        if (this.secret == null || this.secret.isEmpty()) {
            throw new IllegalArgumentException("The Secret cannot be null or empty");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);

            return JWT.create()
                    .withIssuer("auth0")
                    .withSubject(user.getUsername())
                    .withExpiresAt(generateAccessExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while generating token", e);
        }
    }

    public String generateRefreshToken(UserModel user) {
        if (this.secret == null || this.secret.isEmpty()) {
            throw new IllegalArgumentException("The Secret cannot be null or empty");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);

            return JWT.create()
                    .withIssuer("auth0")
                    .withSubject(user.getUsername())
                    .withExpiresAt(generateRefreshTokenExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while generating token", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);

            return JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }

    private Instant generateAccessExpirationDate() {

        return Instant.now().plusMillis(SecurityConstants.JWT_ACCESS_EXPIRATION_TIME);
    }

    private Instant generateRefreshTokenExpirationDate() {

        return Instant.now().plusMillis(SecurityConstants.JWT_REFRESH_TOKEN_EXPIRATION_TIME);
    }
}
