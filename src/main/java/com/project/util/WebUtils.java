package com.project.util;

public class WebUtils {
 
    public static String toString(com.project.model.User loginedUser) {
        StringBuilder sb = new StringBuilder();

        sb.append("UserName:").append(loginedUser.getUsername());

        String authorities = loginedUser.getRole();
        if (authorities != null && !authorities.isEmpty()) {
            sb.append(" ("); 
            sb.append(authorities);
            sb.append(")");
        }
        return sb.toString();
    }
    
}