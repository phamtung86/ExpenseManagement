package com.vti.controller;

import com.vti.form.ChangePasswordForm;
import com.vti.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@Validated
public class UserController {

    @Autowired
    private IUserService userService;

    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable int id, @Valid @RequestBody ChangePasswordForm changePasswordForm) {
        boolean isUpdate = userService.changePassword(id, changePasswordForm);
        return isUpdate  ? ResponseEntity.ok("Change password success")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Change password failed");
    }

}
