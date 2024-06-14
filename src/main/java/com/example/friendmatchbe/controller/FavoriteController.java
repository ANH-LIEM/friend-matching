package com.example.friendmatchbe.controller;

import com.example.friendmatchbe.entity.AddMultiFavoriteDTO;
import com.example.friendmatchbe.entity.Favorite;
import com.example.friendmatchbe.entity.RespEntity;
import com.example.friendmatchbe.entity.User;
import com.example.friendmatchbe.repository.UserFavoriteRepository;
import com.example.friendmatchbe.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserFavoriteRepository userFavoriteRepository;

    @PostMapping
    public ResponseEntity<Favorite> createFavorite(@RequestBody Favorite favorite) {
        return ResponseEntity.ok(favoriteService.save(favorite));
    }

    @PostMapping("/add-multiple-favor")
    public ResponseEntity<RespEntity> addMultipleFavorite(@RequestBody AddMultiFavoriteDTO addMultiFavoriteDTO) {
        String userIp = addMultiFavoriteDTO.getUserIp();
        User user = userRepository.findByUserIp(userIp);
        if (user == null) {
            userService.save(userIp);
        } else {
            userFavoriteRepository.deleteAllByUserId(user.getId());
        }
        List<Long> favoriteIds = addMultiFavoriteDTO.getFavoriteIds();
        for (Long favoriteId : favoriteIds) {
            favoriteService.addFavorite(userIp, favoriteId);
        }
        RespEntity respEntity = new RespEntity();
        respEntity.setResult(true);
        assert user != null;
        respEntity.setId(user.getId());
        return ResponseEntity.ok(respEntity);
    }

    @GetMapping
    public ResponseEntity<List<Favorite>> getAllFavorites() {
        return ResponseEntity.ok(favoriteService.findAll());
    }
}
