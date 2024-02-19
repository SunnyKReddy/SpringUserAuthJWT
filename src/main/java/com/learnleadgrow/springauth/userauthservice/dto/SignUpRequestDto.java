package com.learnleadgrow.springauth.userauthservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SignUpRequestDto {
    private String email;
    private String password;
}
