package com.vadymkykalo.lms.controller;

import com.vadymkykalo.lms.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloWorldController {

    @GetMapping("/hello")
    public ResponseEntity<ApiResponse<String>> sayHello() {

        ApiResponse<String> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setData("Hello, World!");
        response.setMessage("Operation completed successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
