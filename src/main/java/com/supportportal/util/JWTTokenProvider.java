package com.supportportal.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.supportportal.constant.SecurityConstant;
import com.supportportal.domain.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTTokenProvider {
  @Value("{jwt.secret}")
  private String secret;

  public String generateJwtToken(UserPrincipal UserPrincipal) {
    String[] claims = getClaimsFromUser(UserPrincipal);
    return JWT.create()
        .withIssuer(SecurityConstant.GET_ARRAYS_LLC)
        .withAudience(SecurityConstant.GET_ARRAYS_ADMINISTRATION)
        .withIssuedAt(new Date())
        .withSubject(UserPrincipal.getUsername()).withArrayClaim(SecurityConstant.AUTHORITIES, claims)
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(secret.getBytes()));
  }
  private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
    List<String> authorities = new ArrayList<>();
    userPrincipal.getAuthorities()
        .forEach(authority -> authorities.add(authority.getAuthority()));
    return authorities.toArray(new String[0]);
  }
  private JWTVerifier getJWTVerifier() {
    JWTVerifier verifier;
    try {
      Algorithm algorithm = Algorithm.HMAC512(secret);
      verifier = JWT.require(algorithm)
          .withIssuer(SecurityConstant.GET_ARRAYS_LLC)
          .build();
    } catch (JWTVerificationException exception) {
      throw new JWTVerificationException(SecurityConstant.TOKEN_CANNOT_BE_VERIFIED);
    }
    return verifier;
  }

  public boolean isTokenValid(String userName, String token) {
    JWTVerifier verifier = getJWTVerifier();
    return StringUtils.isNotBlank(userName) && !isTokenExpired(verifier, token);
  }
  private boolean isTokenExpired(JWTVerifier verifier, String token) {
    Date expirationDate = verifier.verify(token).getExpiresAt();
    return expirationDate.before(new Date());
  }

  public String getSubject(String token) {
    JWTVerifier verifier = getJWTVerifier();
    return verifier.verify(token).getSubject();
  }

  public List<GrantedAuthority> getAuthorities(String token) {
    String[] claims = getClaimsFromToken(token);
    return Arrays.stream(claims)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }
  private String[] getClaimsFromToken(String token) {
    JWTVerifier verifier = getJWTVerifier();
    return verifier.verify(token)
        .getClaim(SecurityConstant.AUTHORITIES)
        .asArray(String.class);
  }

  public Authentication getAuthentication(String userName, List<GrantedAuthority> authorities, HttpServletRequest request) {
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(userName, null, authorities);
    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    return usernamePasswordAuthenticationToken;
  }
}
