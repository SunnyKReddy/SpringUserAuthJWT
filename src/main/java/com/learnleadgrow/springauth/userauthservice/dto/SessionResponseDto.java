package com.learnleadgrow.springauth.userauthservice.dto;

import com.learnleadgrow.springauth.userauthservice.model.SessionStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class SessionResponseDto {
    private Long userId;
    private Long sessionId;
    private String token;
    private LocalDateTime loginAt;
    private LocalDateTime expiresAt;
    private SessionStatus sessionStatus;

}
