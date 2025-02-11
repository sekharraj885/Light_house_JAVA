package com.lighthouse.controllers;

import com.lighthouse.DTOs.MilestoneDTO;
import com.lighthouse.DTOs.UserDTO;
import com.lighthouse.models.Milestone;
import com.lighthouse.models.User;
import com.lighthouse.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user){
        UserDTO createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<Page<UserDTO>> getAllUsers(String firstName,String lastName, String email, String role, int page, int size){
        Page<UserDTO> users = userService.getAlluser(firstName, lastName, email, role, page,size);
        return  new ResponseEntity<>(users,HttpStatus.OK);
    }

    @GetMapping("/get-user/{id}")
    public ResponseEntity <UserDTO> getMilestoneById(@PathVariable UUID id){
        Optional<UserDTO> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<UserDTO> updateMilestone(@PathVariable UUID id, @RequestBody User user) {
        UserDTO updatedUser = userService.updateUser(id,user);
        return updatedUser != null ?
                new ResponseEntity<>(updatedUser, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
