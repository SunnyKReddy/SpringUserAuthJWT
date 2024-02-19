package com.learnleadgrow.springauth.userauthservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtValidationRequestDto {
    private Long id;
    private String token;
}
