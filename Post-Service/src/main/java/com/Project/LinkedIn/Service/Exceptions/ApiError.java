package com.Project.LinkedIn.Service.Exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class ApiError {
    private LocalDateTime Timestamp;
    private String error;
    private HttpStatus statusCode;

    public ApiError(){
        this.Timestamp=LocalDateTime.now();
    }

    public ApiError(String error,HttpStatus statusCode){
        this();
        this.error=error;
        this.statusCode=statusCode;
    }
}
