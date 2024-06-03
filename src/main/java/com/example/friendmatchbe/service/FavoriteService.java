package com.example.friendmatchbe.service;

import com.example.friendmatchbe.entity.Favorite;
import com.example.friendmatchbe.entity.UserFavorite;
import com.example.friendmatchbe.repository.FavoriteRepository;
import com.example.friendmatchbe.repository.UserFavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserFavoriteRepository userFavoriteRepository;

    public Favorite save(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }
    public List<Favorite> findAll() {
        return favoriteRepository.findAll();
    }
    public Favorite findById(Long id) {
        return favoriteRepository.findById(id).orElse(null);
    }
    public void delete(Long id) {
        favoriteRepository.deleteById(id);
    }

    public List<Favorite> addFavorite(Long userId, Long favoriteId) {
        Optional<UserFavorite> userFavorite = userFavoriteRepository.findAllByUserIdAndFavoriteId(userId, favoriteId);
        if (userFavorite.isEmpty()) {
            UserFavorite userFavoriteEntity = new UserFavorite();
            userFavoriteEntity.setUserId(userId);
            userFavoriteEntity.setFavoriteId(favoriteId);
            userFavoriteRepository.save(userFavoriteEntity);
        }
        List<UserFavorite> userFavorites = userFavoriteRepository.findAllByUserId(userId);
        List<Favorite> favorites = new ArrayList<>();
        for (UserFavorite userFavoriteEntity : userFavorites) {
            favorites.add(favoriteRepository.findById(userFavoriteEntity.getFavoriteId()).orElse(null));
        }
        return favorites;
    }
}
