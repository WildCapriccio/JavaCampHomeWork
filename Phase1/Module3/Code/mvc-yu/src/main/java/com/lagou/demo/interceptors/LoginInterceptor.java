package com.lagou.demo.interceptors;

import com.lagou.edu.mvcframework.interceptors.YuInterceptors;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public class LoginInterceptor implements YuInterceptors {

    private Set<String> allowedUsers = null;

    public Set<String> getAllowedUsers() {
        return allowedUsers;
    }

    public void setAllowedUsers(Set<String> allowedUsers) {
        this.allowedUsers = allowedUsers;
    }

    @Override
    public boolean preHandle(HttpServletRequest request) {
        if (this.allowedUsers == null || this.allowedUsers.size() == 0) {
            return false;
        }
        // Check if current user is in the white-list
        String curName = request.getParameter("username");
        if (allowedUsers.contains(curName)) {
            return true;
        }

        return false;
    }
}
