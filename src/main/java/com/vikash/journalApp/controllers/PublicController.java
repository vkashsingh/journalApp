package com.vikash.journalApp.controllers;

import com.vikash.journalApp.entity.User;
import com.vikash.journalApp.srvices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {


    @Autowired
    private UserService userService;


    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user)
    {
        userService.saveNewUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("health-check")
    public String getHealthStatus()
    {
        return "ok";
    }



}
