package com.saurabh.spring.springauthorizationresourceserver.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.saurabh.spring.springauthorizationresourceserver.custombean.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
	private static Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	public String generateToken(Authentication authentication) {

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Date expiryDate = new Date(System.currentTimeMillis() + 60 * 60 * 1000);

		return Jwts.builder()
				.setSubject(Long.toString(userDetails.getId()))
				.setIssuedAt(new Date())
				.setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, "JWTSuperSecretKey")
				.compact();
	}
	
	public Long getUserIdFromToken(String token){
		Claims claims = Jwts.parser()
				.setSigningKey("JWTSuperSecretKey")
				.parseClaimsJws(token)
				.getBody();
		return Long.parseLong(claims.getSubject());
	}
	
	public boolean validateToken(String authToken){
		
		try{
			Jwts.parser().parseClaimsJws(authToken);
			return true;
		} catch(SignatureException ex){
			logger.error("Invalid Signature");
		} catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
	}
}
