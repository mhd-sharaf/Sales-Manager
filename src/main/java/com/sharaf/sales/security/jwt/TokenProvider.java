package com.sharaf.sales.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TokenProvider {
	private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

	private static final String AUTHORITIES_KEY = "auth";

	private static final String base64_secret = "base64-secret";

	private static final String token_validity_in_seconds = "token-validity-in-seconds";

	private static final String token_validity_in_seconds_for_remember_me = "token-validity-in-seconds-for-remember-me";

	private Key key;

	private long tokenValidityInMilliseconds;

	private long tokenValidityInMillisecondsForRememberMe;

	private final Environment environment;

	public TokenProvider(Environment environment) {
		this.environment = environment;
	}

	@PostConstruct
	public void init() {
		byte[] keyBytes;
		String secret = environment.getProperty(base64_secret);
		if (!StringUtils.isEmpty(secret)) {
			log.warn("Warning: the JWT key used is not Base64-encoded. "
					+ "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security.");
			keyBytes = secret.getBytes(StandardCharsets.UTF_8);
		} else {
			log.debug("Using a Base64-encoded JWT secret key");
			keyBytes = Decoders.BASE64.decode(environment.getProperty(base64_secret));
		}
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.tokenValidityInMilliseconds = 1000 * Long.valueOf(environment.getProperty(token_validity_in_seconds));
		this.tokenValidityInMillisecondsForRememberMe = 1000
				* Long.valueOf(environment.getProperty(token_validity_in_seconds_for_remember_me));
	}

	public String createToken(Authentication authentication, boolean rememberMe) {
		String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validity;
		if (rememberMe) {
			validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
		} else {
			validity = new Date(now + this.tokenValidityInMilliseconds);
		}

		return Jwts.builder().setSubject(authentication.getName()).claim(AUTHORITIES_KEY, authorities)
				.signWith(key, SignatureAlgorithm.HS512).setExpiration(validity).compact();
	}

	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

		Collection<? extends GrantedAuthority> authorities = Arrays
				.stream(claims.get(AUTHORITIES_KEY).toString().split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			log.info("Invalid JWT token.");
			log.trace("Invalid JWT token trace.", e);
		}
		return false;
	}
}
