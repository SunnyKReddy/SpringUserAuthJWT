package com.learnleadgrow.springauth.userauthservice.exception;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionMessage {
    private String message;
    private HttpStatus httpStatus;
}
