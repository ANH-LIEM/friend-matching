package com.example.friendmatchbe.repository;

import com.example.friendmatchbe.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

}
