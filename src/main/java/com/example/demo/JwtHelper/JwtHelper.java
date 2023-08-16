package com.example.demo.JwtHelper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtHelper {
    private static final String SECRET_KEY = "JWT123.!JWT123.!JWT123.!JWT123.!";

    public static String encodeJwt(String subject) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 3600000);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public static String decodeJwt(String jwt) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            return claimsJws.getBody().getSubject();
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }
}
