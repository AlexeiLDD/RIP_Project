package com.rip.RIP_Project.service;

import com.rip.RIP_Project.entity.CustomUser;
import com.rip.RIP_Project.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(CustomUser customUser) {
        customUser.setPassword(passwordEncoder.encode(customUser.getPassword()));
        userRepository.save(customUser);
    }

    public CustomUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
}
