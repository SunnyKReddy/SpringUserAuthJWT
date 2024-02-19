package com.learnleadgrow.springauth.userauthservice.controller;

import com.learnleadgrow.springauth.userauthservice.dto.CreateRoleRequestDto;
import com.learnleadgrow.springauth.userauthservice.model.Role;
import com.learnleadgrow.springauth.userauthservice.repository.RoleRepository;
import com.learnleadgrow.springauth.userauthservice.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {
    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity createRole(@RequestBody CreateRoleRequestDto roleRequestDto) {
        Role role = roleService.createRole(roleRequestDto.getRoleName());
        return new ResponseEntity(role, HttpStatus.CREATED);
    }
}
