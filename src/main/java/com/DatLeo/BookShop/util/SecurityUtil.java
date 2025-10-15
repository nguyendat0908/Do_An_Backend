package com.DatLeo.BookShop.util;

import com.DatLeo.BookShop.dto.request.ReqLoginDTO;
import com.DatLeo.BookShop.dto.response.ResLoginDTO;
import com.DatLeo.BookShop.entity.User;
import com.DatLeo.BookShop.repository.UserRepository;
import com.DatLeo.BookShop.service.AuthService;
import com.nimbusds.jose.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class SecurityUtil {

    private JwtEncoder jwtEncoder;
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    private final UserRepository userRepository;

    @Value("${bookShop.jwt.base64-secret}")
    private String jwtKey;

    @Value("${bookShop.jwt.access-token-validity-in-seconds}")
    private Long accessTokenExpiration;

    @Value("${bookShop.jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;

    public  SecurityUtil(JwtEncoder jwtEncoder, UserRepository userRepository) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
    }

    // Create access token
    public String createAccessToken(String email, ResLoginDTO.UserLogin resLoginDTOUserLogin) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet
                .builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", resLoginDTOUserLogin)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    // Create refresh token
    public String createRefreshToken(String email, ResLoginDTO.UserLogin resLoginDTOUserLogin) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet
                .builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", resLoginDTOUserLogin)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    public Optional<String> getUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    public String getEmailCurrentUserLogin() {
        return getUserLogin().isPresent() ? getUserLogin().get() : "";
    }

    public Integer getIdCurrentUserLogin() {
        String email = getEmailCurrentUserLogin();
        User user = this.userRepository.findByEmail(email);
        return user.getId();
    }


    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length,
                JWT_ALGORITHM.getName());
    }

    public Jwt checkValidRefreshToken(String token) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            System.out.println(">>> Refresh Token error: " + e.getMessage());
            throw e;
        }
    }
}
