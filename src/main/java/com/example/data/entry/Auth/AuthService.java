package com.example.data.entry.Auth;

import lombok.Data;
import org.springframework.stereotype.Service;
@Data
@Service
public class AuthService {

    private String authToken;

    public boolean isTokenPresent() {
        return authToken != null && !authToken.isEmpty();
    }




}
