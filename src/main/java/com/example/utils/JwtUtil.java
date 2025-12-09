package com.example.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Date expiration;

    @Value("${jwt.signature.algorithm}")
    private SignatureAlgorithm signatureAlgorithm;
    
    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(this.jwtSecret);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String id, Map<String, String> extraClaims) {
        Date issuedAt = new Date(System.currentTimeMillis());

        return Jwts.builder()
                .claims(extraClaims) 
                .subject(id) 
                .issuedAt(issuedAt) 
                .expiration(this.expiration) 
                .signWith(getSigningKey(), this.signatureAlgorithm) 
                .compact(); 
    }
}
