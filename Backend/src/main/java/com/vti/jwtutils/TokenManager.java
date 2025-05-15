package com.vti.jwtutils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenManager {
    private static final long TOKEN_VALIDITY = 24 * 60 * 60 ;

    @Value("${secret}")
    private String jwtSecret;

    // Generate JWT Token
    public String generateToken(CustomUserDetails customUserDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", customUserDetails.getUsername());
        claims.put("exp", (new Date().getTime() + TOKEN_VALIDITY));
        claims.put("userId", customUserDetails.getUserId());
        claims.put("fullName", customUserDetails.getFullName());
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(customUserDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(key(), SignatureAlgorithm.HS256)  // Use HS256 for HMAC
                .compact();
    }

    // Validate JWT Token
    public Boolean validateJwtToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        final Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token).getBody();
        Boolean isTokenExpired = claims.getExpiration().before(new Date());
        return (username.equals(userDetails.getUsername())) && !isTokenExpired;
    }
    // Extract username from JWT Token
    public String getUsernameFromToken(String token) {
        final Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    // Create HMAC key from jwtSecret
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
