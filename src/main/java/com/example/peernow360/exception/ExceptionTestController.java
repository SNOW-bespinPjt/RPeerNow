package com.example.peernow360.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExceptionTestController {

    @GetMapping("/users")
    public ResponseEntity<User> getUser() {
        throw new RestApiException(UserErrorCode.inactive_user);

    }

}
