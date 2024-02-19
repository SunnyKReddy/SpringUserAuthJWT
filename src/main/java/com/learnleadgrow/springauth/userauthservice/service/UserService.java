package com.learnleadgrow.springauth.userauthservice.service;

import com.learnleadgrow.springauth.userauthservice.dto.UserDto;
import com.learnleadgrow.springauth.userauthservice.model.Role;
import com.learnleadgrow.springauth.userauthservice.model.User;
import com.learnleadgrow.springauth.userauthservice.repository.RoleRepository;
import com.learnleadgrow.springauth.userauthservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    //Constructor Injection @Autowired is optional as we only have one constructor
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserDto getUserDetails(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            return null;
        }
        return UserDto.from(user.get());
    }

    public UserDto setUserRoles(Long userId, List<Long> roleIds) {
        Set<Role> roles = roleRepository.findAllByIdIn(roleIds);
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isEmpty()){
            return null;
        }

        User user = userOptional.get();
        user.setRoles(roles);
        User updatedUser = userRepository.save(user);

        return UserDto.from(updatedUser);

    }
}
