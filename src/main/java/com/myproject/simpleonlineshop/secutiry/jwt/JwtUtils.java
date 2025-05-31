package com.myproject.simpleonlineshop.secutiry.jwt;

import com.myproject.simpleonlineshop.secutiry.user.MyUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

    @Value("${jwt.secret_key}")
    private String JwtSecretKey;

    @Value("${jwt.expirationInMillis}")
    private int JwtExpirationTimeInMillis;

    public String generateToken(Authentication authentication){
        MyUserDetails userPrincipal = (MyUserDetails) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .claim("id", userPrincipal.getId())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + JwtExpirationTimeInMillis))
                .signWith(key(), SignatureAlgorithm.HS256).compact();

    }
    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JwtSecretKey));
    }
    public String extractUsernameFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(
                        key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
    public boolean isTokenValid(String token ){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key()).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }
}
