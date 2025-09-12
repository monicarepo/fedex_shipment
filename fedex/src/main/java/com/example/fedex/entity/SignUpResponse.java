package com.example.fedex.entity;

import lombok.Data;

@Data
public class SignUpResponse {
    private String message;
    private Boolean success;
    private String error;

    public SignUpResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }

    public SignUpResponse(String error) {
        this.error = error;
        this.success = false;
    }
}
