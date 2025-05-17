package ru.kpfu.itis.kononenko.gtree2.service.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtService {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long ttl = Duration.ofHours(4).toMillis();

    public String generate(UserDetails user) {
        return Jwts.builder()
                   .setSubject(user.getUsername())
                   .claim("roles", user.getAuthorities()
                                       .stream().map(GrantedAuthority::getAuthority).toList())
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + ttl))
                   .signWith(key)
                   .compact();
    }

    public String extractUsername(String token) throws JwtException {
        return Jwts.parserBuilder().setSigningKey(key).build()
                   .parseClaimsJws(token).getBody().getSubject();
    }
}
