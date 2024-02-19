package com.learnleadgrow.springauth.userauthservice.controller;

import com.learnleadgrow.springauth.userauthservice.dto.*;
import com.learnleadgrow.springauth.userauthservice.mapper.ModelMapper;
import com.learnleadgrow.springauth.userauthservice.model.Session;
import com.learnleadgrow.springauth.userauthservice.service.AuthService;
import com.learnleadgrow.springauth.userauthservice.service.UserService;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return authService.signUp(signUpRequestDto);
    }

    @PostMapping("/logout/{id}")
    // @RequestBody LogoutRequestDto logoutRequestDto
    // Passing the token in RequestBody is not the best practice, @RequestBody LogoutRequestDto logoutRequestDto
    // pass the Auth token via RequestHeader
    public ResponseEntity<SessionResponseDto> logout(@PathVariable Long id,
                                                     @RequestHeader("Authorization") String token) {
        return authService.logout(id, token);
    }

    @GetMapping("/sessions/{id}")
    public ResponseEntity<List<SessionResponseDto>> getAllUserSessions(@PathVariable Long id) {
        List<Session> sessions = authService.getAllUserSessions(id);
        List<SessionResponseDto> sessionResponseDtoList = modelMapper.sessionListToSessionResponseDtoList(sessions);
        return new ResponseEntity<>(sessionResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/sessions/active/{id}")
    public ResponseEntity<List<SessionResponseDto>> getUserActiveSessions(@PathVariable Long id) {
        List<Session> sessions = authService.getUserActiveSessions(id);
        List<SessionResponseDto> sessionResponseDtoList = modelMapper.sessionListToSessionResponseDtoList(sessions);
        return new ResponseEntity<>(sessionResponseDtoList, HttpStatus.OK);
    }

    @PostMapping("/validate/jwt")
    // For Validation Method we are passing the JWT token in the request body
    // Passing token in request body is not a best practice but we did for educational/ learning purpose
    public ResponseEntity<JwtValidationResponseDto> validateJwt(@RequestBody JwtValidationRequestDto jwtValidationRequestDto) {
        // if(authService.validateJwt(jwtValidationRequestDto.getId(), jwtValidationRequestDto.getToken())){
            // return new ResponseEntity<>("Valid JWT!", HttpStatus.OK);
            // return null;
        // }
        // return new ResponseEntity<>("Invalid JWT!", HttpStatus.NOT_ACCEPTABLE);
        // return null;
        JwtValidationResponseDto jwtValidationResponseDto = authService.validateJwt(jwtValidationRequestDto.getId(), jwtValidationRequestDto.getToken());
        return new ResponseEntity<>(jwtValidationResponseDto, HttpStatus.OK);
    }

}
