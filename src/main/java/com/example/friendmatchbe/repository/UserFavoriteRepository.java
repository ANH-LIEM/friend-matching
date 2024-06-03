package com.example.friendmatchbe.repository;

import com.example.friendmatchbe.entity.Favorite;
import com.example.friendmatchbe.entity.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {
    List<UserFavorite> findAllByUserId(Long userId);
    List<UserFavorite> findAllByFavoriteId(Long userId);
    Optional<UserFavorite> findAllByUserIdAndFavoriteId(Long userId, Long favoriteId);
}
