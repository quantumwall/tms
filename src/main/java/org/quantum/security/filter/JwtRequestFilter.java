package org.quantum.security.filter;

import java.io.IOException;
import java.util.Objects;

import org.quantum.util.JwtUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

	private final JwtUtils jwtUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		String jwt = null;
		String email = null;
		var header = request.getHeader("Authorization");
		if (Objects.nonNull(header) && header.startsWith("Bearer ")) {
			jwt = header.substring(7);
			try {
				email = jwtUtils.getEmail(jwt);
			} catch (ExpiredJwtException e) {

			} catch (SignatureException e) {

			}
		}
		if (Objects.nonNull(email) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
			var usernamePasswordToken = new UsernamePasswordAuthenticationToken(email, null, null);
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordToken);
		}
		filterChain.doFilter(request, response);
	}

}
