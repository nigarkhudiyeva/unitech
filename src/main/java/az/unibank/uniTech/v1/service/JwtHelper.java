package az.unibank.uniTech.v1.service;

import az.unibank.uniTech.v1.entity.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtHelper {
    private static final String USER_ID = "userId";

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(UserEntity user) {
        return Jwts.builder()
                .claim(USER_ID, user.getId())
                .setExpiration(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();
    }

    public Long extractUserId(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .get(USER_ID, Long.class);
    }
}
