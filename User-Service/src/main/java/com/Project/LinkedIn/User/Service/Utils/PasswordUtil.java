package com.Project.LinkedIn.User.Service.Utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    //hash the password for the first time
    public static String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword,BCrypt.gensalt());
    }

    //check that the plain password Matches the previously hashed one
    public static boolean checkPassword(String plainTextPassword,String hashedPassword){
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
