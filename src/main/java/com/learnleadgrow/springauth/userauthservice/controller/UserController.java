package com.learnleadgrow.springauth.userauthservice.controller;

import com.learnleadgrow.springauth.userauthservice.dto.SetUserRolesRequestDto;
import com.learnleadgrow.springauth.userauthservice.dto.UserDto;
import com.learnleadgrow.springauth.userauthservice.model.User;
import com.learnleadgrow.springauth.userauthservice.service.UserService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity getUser(@PathVariable Long id) {
        UserDto userDto = userService.getUserDetails(id);
        return new ResponseEntity(userDto, HttpStatus.OK);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity setUserRoles(@PathVariable Long id,
                                       @RequestBody SetUserRolesRequestDto setUserRolesRequestDto) {
        UserDto userDto = userService.setUserRoles(id, setUserRolesRequestDto.getRoleIds());
        return new ResponseEntity(userDto, HttpStatus.CREATED);
    }
}
