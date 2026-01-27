package com.vikash.journalApp.controllers;

import com.vikash.journalApp.entity.User;
import com.vikash.journalApp.srvices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;


    @GetMapping("/all-user")
    public ResponseEntity<?> getAllUser()
    {
        List<User> allUser = userService.getAllUser();
        if (!allUser.isEmpty())
        {
            return new ResponseEntity<>(allUser, HttpStatus.OK);
        }
        return  new ResponseEntity<>( HttpStatus.NOT_FOUND);
    }
    @PostMapping("/create-admin")
    public void createAdmin(@RequestBody User user)
    {
        userService.createAdmin(user);
    }
}
