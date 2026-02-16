package com.hospital.backend.controller;


import com.hospital.backend.request.LoginRequest;
import com.hospital.backend.response.ApiResponse;
import com.hospital.backend.response.JwtResponse;
import com.hospital.backend.security.jwt.JwtUtils;
import com.hospital.backend.security.person.HospitalPersonDetails;
import com.hospital.backend.security.person.HospitalPersonDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final HospitalPersonDetailsService hospitalPersonDetailsService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        UserDetails userDetails = hospitalPersonDetailsService.loadUserByEmailAndRole(
                request.getEmail(),
                request.getRoleName()
        );

        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Wrong email or password!");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateTokenForUser(authentication);
        HospitalPersonDetails hospitalDetails = (HospitalPersonDetails) userDetails;

        return ResponseEntity.ok(new ApiResponse("Login Success",
                new JwtResponse(hospitalDetails.getTaj(), jwt)));
    }
}
