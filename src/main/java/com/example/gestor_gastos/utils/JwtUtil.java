package com.example.gestor_gastos.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @Value("${jwt.expiration}")
    private Date EXPIRATION;

    @Value("${jwt.signature.algorithm}")
    private SignatureAlgorithm signatureAlgorithm;

    private final RedisTemplate<String, String> redisTemplate;

    public JwtUtil(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(this.JWT_SECRET);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String id, Map<String, String> extraClaims) {
        Date issuedAt = new Date(System.currentTimeMillis());

        return Jwts.builder()
                .claims(extraClaims) 
                .subject(id) 
                .issuedAt(issuedAt) 
                .expiration(this.EXPIRATION) 
                .signWith(getSigningKey(), this.signatureAlgorithm) 
                .compact(); 
    }

    public void blacklistToken(String jti, long ttlSegundos) {
        this.redisTemplate.opsForValue().set(jti, "revoked", ttlSegundos, TimeUnit.SECONDS);
    }

    public boolean isBlacklisted(String jti) {
        return this.redisTemplate.hasKey(jti);
    }

    public Claims getClaims(String token) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(JWT_SECRET))).build().parseSignedClaims(token).getPayload();
    }
}
