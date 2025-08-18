package com.konecta.internship.Restaurant_POS_System.shared.Exceptions;

import com.konecta.internship.Restaurant_POS_System.auth.security.jwt.exception.JwtTokenExpiredException;
import com.konecta.internship.Restaurant_POS_System.auth.security.jwt.exception.JwtTokenInvalidException;
import com.konecta.internship.Restaurant_POS_System.auth.security.jwt.exception.JwtTokenMissingException;
import com.konecta.internship.Restaurant_POS_System.shared.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.nio.file.AccessDeniedException;
import java.time.Instant;

@RestControllerAdvice
public class ExceptionHandlerAdvice {


    @ExceptionHandler(ObjectNotFoundException.class)
    ResponseEntity<ErrorResponse> handleObjectNotFoundException(ObjectNotFoundException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("NOT_FOUND")
                .message("This object doesn't exist")
                .timestamp(Instant.now())
                .path(getCurrentRequestPath())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class })
    ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .message("email or password is incorrect")
                .timestamp(Instant.now())
                .path(getCurrentRequestPath())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(JwtTokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleJwtTokenExpired(JwtTokenExpiredException ex) {


        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("TOKEN_EXPIRED")
                .message("Your session has expired. Please login again.")
                .timestamp(Instant.now())
                .path(getCurrentRequestPath())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(JwtTokenInvalidException.class)
    public ResponseEntity<ErrorResponse> handleJwtTokenInvalid(JwtTokenInvalidException ex) {


        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("INVALID_TOKEN")
                .message("Invalid authentication token. Please login again.")
                .timestamp(Instant.now())
                .path(getCurrentRequestPath())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(JwtTokenMissingException.class)
    public ResponseEntity<ErrorResponse> handleJwtTokenMissing(JwtTokenMissingException ex) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("TOKEN_MISSING")
                .message("Authentication token is required to access this resource.")
                .timestamp(Instant.now())
                .path(getCurrentRequestPath())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .error("FORBIDDEN")
                .message("You do not have permission to access this resource.")
                .timestamp(Instant.now())
                .path(getCurrentRequestPath())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(org.springframework.security.authorization.AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(org.springframework.security.authorization.AuthorizationDeniedException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .error("FORBIDDEN")
                .message("You do not have permission to access this resource.")
                .timestamp(Instant.now())
                .path(getCurrentRequestPath())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherException(Exception ex) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("error")
                .message("A server internal error ")
                .timestamp(Instant.now())
                .path(getCurrentRequestPath())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }




    private String getCurrentRequestPath() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
            return request.getRequestURI();
        } catch (Exception e) {
            return "unknown";
        }
    }
}
