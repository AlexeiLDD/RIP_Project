package com.rip.RIP_Project.controller;


import com.rip.RIP_Project.dto.auth.AuthRequest;
import com.rip.RIP_Project.dto.auth.AuthResponse;
import com.rip.RIP_Project.entity.CustomUser;
import com.rip.RIP_Project.service.JwtService;
import com.rip.RIP_Project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authenticationRequest) throws Exception {
        final String jwt = jwtService.createJwtToken(authenticationRequest);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody CustomUser customUser) {
        if (userService.findByUsername(customUser.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username is already taken.");
        }
        userService.save(customUser);
        return ResponseEntity.ok("User registered successfully.");
    }
}
