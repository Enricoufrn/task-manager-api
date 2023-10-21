package com.example.taskmanagerapi.services;

import com.example.taskmanagerapi.domain.enumerations.MessageCode;
import com.example.taskmanagerapi.exceptions.ExtractTokenClaimException;
import com.example.taskmanagerapi.exceptions.GenerateTokenException;
import com.example.taskmanagerapi.helper.MessageHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements IJwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration_time}")
    private long expirationTime;
    private final MessageHelper messageHelper;

    public JwtServiceImpl(MessageHelper messageHelper) {
        this.messageHelper = messageHelper;
    }

    @Override
    public String extractLogin(String token){
        try {
            return extractClaim(token, Claims::getSubject);
        }catch (Exception e) {
            throw new ExtractTokenClaimException(this.messageHelper.getMessage(MessageCode.INVALID_AUTHORIZATION_HEADER), this.messageHelper.getMessage(MessageCode.INVALID_TOKEN));
        }
    }

    @Override
    public String generateToken(UserDetails userDetails){
        if (Objects.isNull(userDetails)){
            throw new GenerateTokenException(this.messageHelper.getMessage(MessageCode.AUTHENTICATION_NOT_FINALLY),
                    this.messageHelper.getMessage(MessageCode.INTERNAL_SERVER_ERROR));
        }else return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public boolean isValidToken(String token, UserDetails userDetails) {
        if (token == null || token.isEmpty() || userDetails == null){
            return false;
        }
        final String userName = extractLogin(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if token is expired
     * @param token jwt token
     * @return true if token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts expiration date from token
     * @param token jwt token
     * @return expiration date
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts claim from token
     * @param token jwt token
     * @param claimsResolver claims resolver
     * @param <T> role of claim
     * @return claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from token
     * @param token jwt token
     * @return claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Generates signing key from secret key
     * @return signing key
     */
    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates new jwt token
     * @param extraClaims extra claims
     * @param userDetails user details object
     * @return jwt token
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
