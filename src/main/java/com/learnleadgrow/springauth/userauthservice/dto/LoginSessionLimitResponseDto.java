package com.learnleadgrow.springauth.userauthservice.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class LoginSessionLimitResponseDto {
    private List<SessionResponseDto> sessionResponseDtoList;
    private String exceptionMessage;
    private HttpStatus httpStatus;
}
