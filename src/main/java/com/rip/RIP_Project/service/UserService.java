package com.rip.RIP_Project.service;

import com.rip.RIP_Project.entity.CustomUser;
import com.rip.RIP_Project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CustomUser save(CustomUser customUser) {
        customUser.setPassword(passwordEncoder.encode(customUser.getPassword()));
        return userRepository.save(customUser);
    }

    public CustomUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
