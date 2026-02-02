package com.Project.LinkedIn.Service.Exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message, Long postId){
        super(message);
    }
}
