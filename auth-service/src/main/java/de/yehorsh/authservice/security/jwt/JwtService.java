package de.yehorsh.authservice.security.jwt;

import de.yehorsh.authservice.dto.JwtAuthenticationDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Setter
@Service
public class JwtService {
    private static final Logger LOGGER = LogManager.getLogger(JwtService.class);

    @Value("d91fb2a36c4ad89da9f322bcfcd7b357294eb77e8c44d5b36695f449ddacc76f")
    public String jwtSecret;

    @Value("${jwt.token.ttl-minutes:60}")
    private long tokenTtlMinutes;

    public JwtAuthenticationDto generateAuthToken(String email) {
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(email));
        jwtDto.setRefreshToken(generateRefreshToken(email));
        return jwtDto;
    }

    public JwtAuthenticationDto refreshBaseToken(String email, String refreshToken) {
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(email));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(getSingInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSingInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (ExpiredJwtException expEx) {
            LOGGER.error("Expired JwtException", expEx);
        } catch (UnsupportedJwtException expEx) {
            LOGGER.error("Unsupported JwtException", expEx);
        } catch (MalformedJwtException expEx) {
            LOGGER.error("Malformed JwtException", expEx);
        } catch (SecurityException expEx) {
            LOGGER.error("Security Exception", expEx);
        } catch (Exception expEx) {
            LOGGER.error("invalid token", expEx);
        }
        return false;
    }

    private String generateJwtToken(String email) {
        Date data = Date.from(
                LocalDateTime.now()
                        .plusMinutes(tokenTtlMinutes)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );

        return Jwts.builder()
                .subject(email)
                .expiration(data)
                .signWith(getSingInKey())
                .compact();
    }

    private String generateRefreshToken(String email) {
        Date date = Date.from(
                LocalDateTime.now()
                        .plusDays(1)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
        return Jwts.builder()
                .subject(email)
                .expiration(date)
                .signWith(getSingInKey())
                .compact();
    }

    private SecretKey getSingInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
