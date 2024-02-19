package com.learnleadgrow.springauth.userauthservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseModel {
    private String email;
    private String password;
    @ManyToMany
    private Set<Role> roles = new HashSet<>();
}
