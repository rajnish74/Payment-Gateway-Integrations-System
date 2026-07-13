package com.rajnish.razorpay.merchant.security;

import com.rajnish.razorpay.merchant.entity.ApiKey;
import com.rajnish.razorpay.merchant.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {


    public static final String BASIC_PREFIX = "Basic ";
    private final ApiKeyRepository apiKeyRepository;
    private final BCryptPasswordEncoder BCRYPT = new BCryptPasswordEncoder();
    private final MerchantContext merchantContext;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("Incoming request: {}", request.getRequestURI());

        try {
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith(BASIC_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            String[] credentials = decodeHeader(authorizationHeader);
            if (credentials == null) {
                throw new BadCredentialsException("Malformed API key header");
            }

            String keyId = credentials[0];
            String rawSecret = credentials[1];

            ApiKey apiKey = apiKeyRepository.findByKeyId(keyId)
                    .orElseThrow(() -> new BadCredentialsException("Invalid or missing API key"));


            if (!apiKey.isEnabled() || !secretMatches(rawSecret, apiKey)) {
                throw new BadCredentialsException("Invalid or missing API key");
            }

            var auth = new UsernamePasswordAuthenticationToken(keyId, null,
                    List.of(new SimpleGrantedAuthority("API_KEY_ROLE"))
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
            merchantContext.setMerchantId(apiKey.getMerchant().getId());
            merchantContext.setKeyId(apiKey.getKeyId());

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("Error during API key authentication: {}", e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        }



    }

    private boolean secretMatches(String rawSecret, ApiKey apiKey) {
        if (BCRYPT.matches(rawSecret, apiKey.getKeySecretHash())){
            return true;
        }
        boolean isInGracePeriod = apiKey.getGracePeriodExpiresAt() != null &&
                LocalDateTime.now().isBefore(apiKey.getGracePeriodExpiresAt());
        return isInGracePeriod
                && apiKey.getGracePeriodExpiresAt() != null
                && BCRYPT.matches(rawSecret, apiKey.getPreviousKeySecretHash());
    }

    private String[] decodeHeader(String header) {
        String encodedHeader = header.substring(BASIC_PREFIX.length());
        String decoded=new String(Base64.getDecoder().decode(encodedHeader), StandardCharsets.UTF_8);

        int colon = decoded.indexOf(':');
        if(colon < 1) return null;
        return new String[]{decoded.substring(0, colon), decoded.substring(colon+1)};
    }
}
