package com.example.friendmatchbe.controller;

import com.example.friendmatchbe.entity.Favorite;
import com.example.friendmatchbe.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Favorite> createFavorite(@RequestBody Favorite favorite) {
        return ResponseEntity.ok(favoriteService.save(favorite));
    }

    @PostMapping("/add-favor")
    public ResponseEntity<List<Favorite>> addFavorite(@RequestParam Long userId, @RequestParam Long favoriteId) {
        return ResponseEntity.ok(favoriteService.addFavorite(userId, favoriteId));
    }

    @GetMapping
    public ResponseEntity<List<Favorite>> getAllFavorites() {
        return ResponseEntity.ok(favoriteService.findAll());
    }
}
