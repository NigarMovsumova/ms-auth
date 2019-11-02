package az.technical.task.msauth.security.util;

import az.technical.task.msauth.security.model.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class TokenUtils {

    private Clock clock = DefaultClock.INSTANCE;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public UserInfo getUserInfoFromToken(String token) {
        String customerId = getClaimFromToken(token, Claims::getId);
        String email = getClaimFromToken(token, Claims::getSubject);
        String role= getAllClaimsFromToken(token).get("role").toString();
        UserInfo userInfo = UserInfo
                .builder()
                .token(token)
                .role(role)
                .customerId(customerId)
                .email(email)
                .build();
        return userInfo;
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(String username, String customerId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("customerId", customerId);
        claims.put("role", role);
        return doGenerateToken(claims, username, customerId);
    }

    public String doGenerateToken(Map<String, Object> claims,
                                  String subject, String customerId) {
        Date createdDate = clock.now();
        Date expirationDate = calculateExpirationDate(createdDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)//username
                .setId(customerId)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 100);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        if (Objects.isNull(userDetails) || Objects.isNull(token)) {
            return false;
        }
        String username = getUsernameFromToken(token);

        return Objects.equals(
                username, userDetails.getUsername())
                && !isTokenExpired(token);
    }


    public boolean isTokenValid(String token) {

        if (Objects.isNull(token)) {
            return false;
        }

        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(clock.now());
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

}