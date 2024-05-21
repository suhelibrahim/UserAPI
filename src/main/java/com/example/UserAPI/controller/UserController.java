package com.example.UserAPI.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.UserAPI.interceptor.UserInterceptor;
import com.example.UserAPI.model.User;
import com.example.UserAPI.repo.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class UserController extends UserInterceptor {

    @Autowired
    private UserInterceptor userInterceptor;

    @Autowired
    private UserRepo userRepo;

    // Get Users Api
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {

        HttpHeaders headers = new HttpHeaders();
        System.out.println("headers====>" + headers);
        try {
            List<User> userList = new ArrayList<>();
            userRepo.findAll().forEach(userList::add);

            if (userList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User userObj = userRepo.save(user);
        return new ResponseEntity<>(userObj, HttpStatus.OK);
    }

     @GetMapping("/user/{id}")
    public String getUserById(@RequestParam String param) {
        return new String();
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable Long id, @RequestBody User newUser) {
        
        Optional<User> oldUser = userRepo.findById(id);
        if(oldUser.isPresent()) {
            User updateUser = oldUser.get();
            updateUser.setName(newUser.getName());
            updateUser.setEmail(newUser.getEmail());
            userRepo.save(updateUser);
            return new ResponseEntity<>(updateUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(newUser, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        Optional<User> existingUser = userRepo.findById(id);
        if(existingUser.isPresent()) {
            userRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("User has been deleted successfully",HttpStatus.BAD_REQUEST);
    }
    

}
