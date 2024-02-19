package com.learnleadgrow.springauth.userauthservice.service;

import com.learnleadgrow.springauth.userauthservice.model.Role;
import com.learnleadgrow.springauth.userauthservice.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    //Constructor Injection @Autowired is optional as we only have one constructor
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(String roleName) {
        Role role = new Role();
        role.setRole(roleName);

        Role savedRole = roleRepository.save(role);

        return savedRole;

    }
}
