package com.konecta.internship.Restaurant_POS_System.auth.dto.response;

import com.konecta.internship.Restaurant_POS_System.User.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private UserRole role;
}
