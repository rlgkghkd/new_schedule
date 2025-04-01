package com.example.newschedule.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokenDto {
    @NotEmpty
    private String grantType;
    @NotEmpty
    private String accessToken;
    @NotEmpty
    private String refreshToken;
}
