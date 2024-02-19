package com.learnleadgrow.springauth.userauthservice.security.jwtconfig;

import com.learnleadgrow.springauth.userauthservice.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {
    public String generateJwt() {
        //Secret for Signing Key
        String secretKey = "mysecretmysecretmysecretmysecretmysecretmysecret";
        //Creating claim
        //String claim = "username";
        //String value = "MSKousikReddy";
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("userName", "SunnyKousik");
        jsonData.put("roles", List.of("ADMIN", "MANAGER"));
        jsonData.put("createdAt", new Date());
        jsonData.put("expiryAt", new Date(LocalDate.now().plusDays(1).toEpochDay()));
        //Create JWT
        String token = Jwts.builder()
                .addClaims(jsonData)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        System.out.println("Generated JWT token is: " + token);
        // Sample JWT: token
        // eyJhbGciOiJIUzI1NiJ9 // Header
        // .eyJjcmVhdGVkQXQiOjE3MDgxMzk4NzQzMjEsInJvbGVzIjpbIkFETUlOIiwiTUFOQUdFUiJdLCJleHBpcnlBdCI6MTk3NzEsInVzZXJOYW1lIjoiU3VubnlLb3VzaWsifQ // Payload
        // .EAEhN7bKCuSVdLc6tgTRCmZ2XOW-GjGN4438_M0XLDA // Signature
        return token;
    }

    public Boolean validateJwt() {
        //Secret for Signing Key
        String secretKey = "mysecretmysecretmysecretmysecretmysecretmysecret";
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJjcmVhdGVkQXQiOjE3MDgxMzk4NzQzMjEsInJvbGVzIjpbIkFETUlOIiwiTUFOQUdFUiJdLCJleHBpcnlBdCI6MTk3NzEsInVzZXJOYW1lIjoiU3VubnlLb3VzaWsifW.VvOtJG8UsZeLVYxNoXJjDabjhQ1Iiri1_1CIf7RiQCM";
        try{
//            Jws<Claims> claims = Jwts.parser()
//                    .setSigningKey(secretKey)
//                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException(e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
        return true;
    }
}
