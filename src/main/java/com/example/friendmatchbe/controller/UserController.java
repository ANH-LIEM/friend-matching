package com.example.friendmatchbe.controller;

import com.example.friendmatchbe.entity.User;
import com.example.friendmatchbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }
    @GetMapping("/findByFavorId")
    public ResponseEntity<List<User>> findAllByFavorite(@RequestParam Long favoriteId) {
        return ResponseEntity.ok(userService.findAllByFavorite(favoriteId));
    }

}
