package com.example.friendmatchbe.controller;

import com.example.friendmatchbe.entity.AddFriendRequest;
import com.example.friendmatchbe.entity.User;
import com.example.friendmatchbe.entity.UserUser;
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

    @GetMapping("/findFriends")
    public ResponseEntity<List<User>> findAllFriends(@RequestParam Long userId) {
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
    public ResponseEntity<UserUser> acceptFriendRequest(AddFriendRequest addFriendRequest){
        return ResponseEntity.ok(userService.acceptRequest(addFriendRequest));
    }

}
