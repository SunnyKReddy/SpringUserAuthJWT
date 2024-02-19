package com.learnleadgrow.springauth.userauthservice.controller;

import com.learnleadgrow.springauth.userauthservice.security.jwtconfig.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private JwtService jwtService;

    @GetMapping("/jwt/generate")
    public ResponseEntity generateToken() {
        return new ResponseEntity(jwtService.generateJwt(), HttpStatus.CREATED);
    }

    @GetMapping("/jwt/validate")
    public ResponseEntity validateToken() {
        if(jwtService.validateJwt()) {
            return new ResponseEntity<>("Valid JWT!", HttpStatus.OK);
        }else{
            return new ResponseEntity("InValid JWT!", HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
