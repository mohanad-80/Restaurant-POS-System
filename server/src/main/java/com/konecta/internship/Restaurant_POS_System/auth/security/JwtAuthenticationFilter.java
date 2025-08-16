package com.konecta.internship.Restaurant_POS_System.auth.security;

import com.konecta.internship.Restaurant_POS_System.auth.security.jwt.exception.JwtTokenExpiredException;
import com.konecta.internship.Restaurant_POS_System.auth.security.jwt.exception.JwtTokenInvalidException;
import com.konecta.internship.Restaurant_POS_System.auth.security.jwt.exception.JwtTokenMissingException;
import com.konecta.internship.Restaurant_POS_System.auth.security.jwt.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtService;
    private final UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = jwtService.getJwtFromHeader(request);

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String userEmail = jwtService.extractUsername(token);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }catch (JwtTokenExpiredException | JwtTokenInvalidException | JwtTokenMissingException e) {

            logger.debug("JWT exception occurred: {}", e.getMessage());
            request.setAttribute("exception", e);
        } catch (Exception e) {
            logger.error("Unexpected error in JWT filter: {}", e.getMessage(), e);
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }
}
