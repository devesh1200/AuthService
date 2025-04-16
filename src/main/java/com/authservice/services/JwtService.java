package com.authservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    public static final String SECRET = "6a0703b6c3f153253bda948eaed7e5606e1a12dcb58108f824be767dad92d552";

    public String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .claims(claims) // Use .claims() instead of .setClaims()
                .subject(username) // Use .subject() instead of .setSubject()
                .issuedAt(new Date(System.currentTimeMillis())) // Use .issuedAt() instead of .setIssuedAt()
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 1)) // 1 minute expiration
                .signWith(getSignKey(), Jwts.SIG.HS256) // Use Jwts.SIG.HS256 instead of SignatureAlgorithm.HS256
                .compact();
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey()) // Use .verifyWith() instead of .setSigningKey()
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET); // Decode the secret key from Base64
        return Keys.hmacShaKeyFor(keyBytes); // Generate the SecretKey
    }
}