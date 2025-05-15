package com.vti.controller;

import com.vti.dto.ErrorResponse;
import com.vti.entity.User;
import com.vti.form.CreateUserForm;
import com.vti.jwtutils.CustomUserDetails;
import com.vti.jwtutils.JwtUserDetailsService;
import com.vti.jwtutils.TokenManager;
import com.vti.models.JwtRequestModel;
import com.vti.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> createToken(@RequestBody JwtRequestModel request) {
        User user = userService.findUserByPhoneNumber(request.getPhoneNumber());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Account does not exist"));
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getPhoneNumber(), request.getPassword()));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("User is disabled"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid username or password"));
        }

        final CustomUserDetails userDetails = jwtUserDetailsService.loadUserByUsername(request.getPhoneNumber());
        System.out.println(userDetails.getUserId());
        System.out.println(userDetails.getFullName());
        final String jwtToken = tokenManager.generateToken(userDetails);
        return ResponseEntity.ok(Map.of(
                "token", jwtToken,
                "user", Map.of(
                        "id", userDetails.getUserId(),
                        "fullName", userDetails.getFullName()
                )
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@Valid @RequestBody CreateUserForm createUserForm) {
        User user = userService.createUser(createUserForm);
        return user != null ? ResponseEntity.status(200).body("Create user success") : ResponseEntity.notFound().build();
    }
}
