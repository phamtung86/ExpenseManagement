package com.vti.controller;

import com.vti.dto.UserDTO;
import com.vti.form.ChangePasswordForm;
import com.vti.form.UpdateUserForm;
import com.vti.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/user")
@RestController
@Validated
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable(name = "id") int id) {
        UserDTO userDTO = userService.getUser(id);
        return ResponseEntity.status(200).body(userDTO);
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable int id, @Valid @RequestBody ChangePasswordForm changePasswordForm) {
        boolean isUpdate = userService.changePassword(id, changePasswordForm);
        return isUpdate  ? ResponseEntity.ok("Change password success")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Change password failed");
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @Valid @RequestBody UpdateUserForm updateUserForm) {
        boolean isUpdate = userService.updateUser(id, updateUserForm);
        if(isUpdate){
            return ResponseEntity.ok("Update user success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update user failed");
    }

    @PutMapping("/update-notice/{id}")
    public ResponseEntity<?> updateNotice(@PathVariable(name = "id") Integer userId, @RequestParam(name = "notice") boolean notice){
        userService.updateNotice(userId, notice);
        return ResponseEntity.ok("Update notice success");
    }


//
//    @GetMapping("/get-user-info/{id}")
//    public  ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer id){
//        UserResponseDTO userResponseDTO = userService.getUserById(id);
//        return ResponseEntity.ok(userResponseDTO);
//    }

//    @PutMapping("/update-user-info/{id}")
//    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Integer id, @RequestBody @Valid UserRequestDTO userRequestDTO){
//        UserResponseDTO userResponseDTO = userService.updateUser(id, userRequestDTO);
//        return ResponseEntity.ok(userResponseDTO);
//    }

}
