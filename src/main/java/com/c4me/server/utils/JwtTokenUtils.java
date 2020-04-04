package com.c4me.server.utils;

import com.c4me.server.core.credential.domain.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import javax.xml.bind.DatatypeConverter;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-22-2020
 */
public class JwtTokenUtils {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    private static final String SECRET = "c4meSecret";
    private static final String ISS = "C4meAuth";
    private static final String ROLE_CLAIM = "rol";
    private static final String USE_ID = "uid";
    

    // 1 hour token
    private static final long EXPIRATION = 3600L;

    // 7 days token Remember me
    private static final long EXPIRATION_REMEMBER = 604800L;


    // create token
    public static String createToken(JwtUser user, boolean isRememberMe) {
        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;

        HashMap<String, Object> claimMap = new HashMap<>();
        claimMap.put(ROLE_CLAIM, user.getAuthorities().get(0).getAuthority());
        claimMap.put(USE_ID, user.getId());

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .setClaims(claimMap)
                .setIssuer(ISS)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    public static Boolean checkJWT(String jsonWebToken){
        try {
            Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                .parseClaimsJws(jsonWebToken);
        }catch (Exception e) {
            return false;
        }
        return true;
    }

    // get userId from token
    public static UUID getUserId(String token) throws ExpiredJwtException{
        return UUID.fromString(getTokenBody(token).get(USE_ID).toString());
    }

    // get username from token
    public static String getUsername(String token) throws ExpiredJwtException{
        return getTokenBody(token).getSubject();
    }

    // get authorities
    public static String getUserRole(String token) throws ExpiredJwtException{
        return (String) getTokenBody(token).get(ROLE_CLAIM);
    }

    private static Claims getTokenBody(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
