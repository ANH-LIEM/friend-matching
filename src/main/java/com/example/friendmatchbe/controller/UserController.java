package com.example.friendmatchbe.controller;

import com.example.friendmatchbe.entity.AddFriendRequest;
import com.example.friendmatchbe.entity.RecommendResponse;
import com.example.friendmatchbe.entity.User;
import com.example.friendmatchbe.entity.UserUser;
import com.example.friendmatchbe.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/findByFavorId")
    public ResponseEntity<List<User>> findAllByFavorite(@RequestParam Long favoriteId) {
        return ResponseEntity.ok(userService.findAllByFavorite(favoriteId));
    }

    @GetMapping("/findFriends")
    public ResponseEntity<List<User>> findAllFriends(@RequestParam String userIp) {
        Long userId = userRepository.findByUserIp(userIp).getId();
        return ResponseEntity.ok(userService.findAllFriends(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id).orElse(null));
    }

    @GetMapping("/addFriendRequest")
    public ResponseEntity<AddFriendRequest> addFriendRequest(@RequestBody AddFriendRequest addFriendRequest) {
        return ResponseEntity.ok(userService.addFriendRequest(addFriendRequest));
    }

    @GetMapping("/findAllAddFriendRequest")
    public ResponseEntity<List<AddFriendRequest>> findAllAddFriendRequest(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.findAllAddFriendRequest(userId));
    }

    @GetMapping("/acceptFriendRequest")
    public ResponseEntity<UserUser> acceptFriendRequest(@RequestParam Long addFriendRequestId){
        return ResponseEntity.ok(userService.acceptRequest(addFriendRequestId));
    }

    @GetMapping("/recommendFriends")
    public ResponseEntity<List<RecommendResponse>> recommendFriends(@RequestParam String userIp) {
        Long userId = userRepository.findByUserIp(userIp).getId();
        return ResponseEntity.ok(userService.recommendFriends(userId));
    }

}
