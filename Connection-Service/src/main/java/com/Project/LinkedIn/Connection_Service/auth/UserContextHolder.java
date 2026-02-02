package com.Project.LinkedIn.Connection_Service.auth;

public class UserContextHolder {
    private static final ThreadLocal<Long> currentUserId=new ThreadLocal<>();  // one thread per request

    public static Long getCurrentUserId(){
        return currentUserId.get();
    }     // getting

    public static void setCurrentUserId(Long userId){
        currentUserId.set(userId);
    }    // setting

    static void clear(){
        currentUserId.remove();
    }     //after controller interceptor called and cleared
}
