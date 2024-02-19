package com.learnleadgrow.springauth.userauthservice.dto;

import com.learnleadgrow.springauth.userauthservice.model.Role;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class JwtValidationResponseDto {
    private Integer userId;
    private String username;
    private List<Role> roles;
    private Long createdAt;
    private Integer expiryAt;
    private String message;
}
