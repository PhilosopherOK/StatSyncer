package com.example.statsyncer.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {


    private static final String SECRET_KEY = "f7db991b0f52da64d382ffa5bf0df91ae1dfb60295b17014f3f8c64169820331";

    // Метод для отримання імені користувача з токену
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Метод для отримання конкретної вимоги (claim) з токену
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Метод для генерації токену з інформацією про користувача
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    // Метод для генерації токену з додатковими даними (claims) про користувача
    public String generateToken(Map<String ,Object> extraClaims,
                                UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*24))
                .signWith(getSingInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Метод для перевірки дійсності токену
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Метод для перевірки, чи минув термін дії токену
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Метод для отримання дати закінчення терміну дії токену
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Метод для отримання всіх claims з токену
    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSingInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Метод для отримання секретного ключа для підписання токену
    private Key getSingInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

