package com.example.data.entry.Auth;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@RestController
public class AuthController {

    private final String authServiceUrl = "http://auth-app:8081/api/v1/auth"; // URL of the Authentication Service

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthService authService;


    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.setAuthToken("");
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Create HttpEntity of the request
        HttpEntity<RegisterRequest> requestEntity = new HttpEntity<>(request);

        // Send POST request to the Authentication Service and receive a response
        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(authServiceUrl + "/register", requestEntity, AuthenticationResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            authService.setAuthToken(Objects.requireNonNull(response.getBody()).getToken());
            return ResponseEntity.ok("Registered and authenticated");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Registration/Authentication failed");
        }
    }


    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody RegisterRequest request) {
        // Create HttpEntity of the request
        HttpEntity<RegisterRequest> requestEntity = new HttpEntity<>(request);

        // Send POST request to the Authentication Service and receive a response
        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(authServiceUrl + "/authenticate", requestEntity, AuthenticationResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            authService.setAuthToken(Objects.requireNonNull(response.getBody()).getToken());
            return ResponseEntity.ok("authenticated");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Authentication failed");
        }
    }

}