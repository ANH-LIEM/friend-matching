package com.example.friendmatchbe.controller;

import com.example.friendmatchbe.entity.AddMultiFavoriteDTO;
import com.example.friendmatchbe.entity.Favorite;
import com.example.friendmatchbe.entity.User;
import com.example.friendmatchbe.service.FavoriteService;
import com.example.friendmatchbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Favorite> createFavorite(@RequestBody Favorite favorite) {
        return ResponseEntity.ok(favoriteService.save(favorite));
    }

    @PostMapping("/add-multiple-favor")
    public ResponseEntity<Map<String,Boolean>> addMultipleFavorite(@RequestBody AddMultiFavoriteDTO addMultiFavoriteDTO) {
        System.out.println(addMultiFavoriteDTO.toString());
        String userIp = addMultiFavoriteDTO.getUserIp();
        userService.save(userIp);
        List<Long> favoriteIds = addMultiFavoriteDTO.getFavoriteIds();
        for (Long favoriteId : favoriteIds) {
            favoriteService.addFavorite(userIp, favoriteId);
        }
        return ResponseEntity.ok(Map.of("result", true));
    }

    @GetMapping
    public ResponseEntity<List<Favorite>> getAllFavorites() {
        return ResponseEntity.ok(favoriteService.findAll());
    }
}
