package org.quantum.util;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.quantum.entity.User;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;

@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {

	private String secret;
	private Duration lifetime;

	public String generateToken(User user) {
		var key = Keys.hmacShaKeyFor(secret.getBytes());
		var issuedAt = new Date();
		var expiration = new Date(issuedAt.getTime() + lifetime.toMillis());
		var claims = new HashMap<String, Object>();
		claims.put("email", user.getEmail());
		return Jwts.builder().claims(claims).issuedAt(issuedAt).expiration(expiration).signWith(key).compact();
	}

	public String geEmail(String token) {
		return getClaims(token).get("email", String.class);
	}

	@SuppressWarnings("unchecked")
	public List<String> getRoles(String token) {
		return getClaims(token).get("roles", List.class);
	}

	public Claims getClaims(String token) {
		var key = Keys.hmacShaKeyFor(secret.getBytes());
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
	}
}
