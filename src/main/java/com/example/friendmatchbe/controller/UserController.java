package com.example.friendmatchbe.controller;

import com.example.friendmatchbe.entity.AddFriendRequest;
import com.example.friendmatchbe.entity.RecommendResponse;
import com.example.friendmatchbe.entity.User;
import com.example.friendmatchbe.entity.UserUser;
import com.example.friendmatchbe.repository.AddFriendRequestRepository;
import com.example.friendmatchbe.repository.UserRepository;
import com.example.friendmatchbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddFriendRequestRepository addFriendRequestRepository;

    @GetMapping("/findByFavorId")
    public ResponseEntity<List<User>> findAllByFavorite(@RequestParam Long favoriteId) {
        return ResponseEntity.ok(userService.findAllByFavorite(favoriteId));
    }

    @GetMapping("/findFriends")
    public ResponseEntity<List<RecommendResponse>> findAllFriends(@RequestParam String userIp) {
        Long userId = userRepository.findByUserIp(userIp).getId();
        return ResponseEntity.ok(userService.findAllFriendsNew(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id).orElse(null));
    }

    @PostMapping("/addFriendRequest")
    public ResponseEntity<Map<String,Boolean>> addFriendRequest(@RequestBody AddFriendRequest addFriendRequest) {
        addFriendRequest.setFirstUserId(userRepository.findByUserIp(addFriendRequest.getFirstUserIp()).getId());
        System.out.println(addFriendRequest.getFirstUserId() + " " + addFriendRequest.getSecondUserId());
        userService.addFriendRequest(addFriendRequest);
        return ResponseEntity.ok(Map.of("result", true));
    }

    @GetMapping("/findAllAddFriendRequest")
    public ResponseEntity<List<RecommendResponse>> findAllAddFriendRequest(@RequestParam String userIp) {
        Long userId = userRepository.findByUserIp(userIp).getId();
        return ResponseEntity.ok(userService.findAllAddFriendRequest(userId));
    }

    @PostMapping("/acceptFriendRequest")
    public ResponseEntity<Map<String,Boolean>> acceptFriendRequest(@RequestBody AddFriendRequest addFriendRequest){
        Long userId = userRepository.findByUserIp(addFriendRequest.getFirstUserIp()).getId();
        Long addFriendRequestId = addFriendRequestRepository.findByFirstUserIdAndSecondUserId(addFriendRequest.getSecondUserId(),userId).getId();

        userService.acceptRequest(addFriendRequestId);
        return ResponseEntity.ok(Map.of("result", true));
    }

    @GetMapping("/recommendFriends")
    public ResponseEntity<List<RecommendResponse>> recommendFriends(@RequestParam String userIp) {
        Long userId = userRepository.findByUserIp(userIp).getId();
        return ResponseEntity.ok(userService.recommendFriends(userId));
    }

}
