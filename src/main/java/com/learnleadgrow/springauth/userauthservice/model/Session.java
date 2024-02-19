package com.learnleadgrow.springauth.userauthservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity(name = "sessions")
public class Session extends BaseModel {
    private String token;
    private LocalDateTime expiresAt;
    private LocalDateTime loginAt;
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus sessionStatus;
    @ManyToOne
    private User user;
    private SecretKey secretKey;
}
