package com.myproject.simpleonlineshop.controller;


import com.myproject.simpleonlineshop.dto.ApiResponse;
import com.myproject.simpleonlineshop.dto.JwtResponse;
import com.myproject.simpleonlineshop.dto.LoginRequest;
import com.myproject.simpleonlineshop.secutiry.jwt.JwtUtils;
import com.myproject.simpleonlineshop.secutiry.user.MyUserDetails;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login( @Valid @RequestBody LoginRequest request){
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateToken(authentication);
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            log.info(userDetails.getAuthorities().stream().map(s -> s.toString()).toList() + " ");
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(),jwt);
            return ResponseEntity.ok(new ApiResponse(" Successful login", jwtResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Invalid Email Or Password", e.getMessage()));
        }
    }
}
