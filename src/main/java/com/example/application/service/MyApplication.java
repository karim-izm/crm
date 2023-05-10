package com.example.application.service;

import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.HashSet;
import java.util.Set;

public class MyApplication extends SpringBootServletInitializer {
    private static final Set<String> connectedUsers = new HashSet<>();

    public static Set<String> getConnectedUsers() {
        return connectedUsers;
    }
}
