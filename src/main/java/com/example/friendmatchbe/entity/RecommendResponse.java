package com.example.friendmatchbe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecommendResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double score;
    private List<Favorite> favoritesOverlap;
    private List<Favorite> favoritesOther;

    public RecommendResponse(User friendOfFavorite) {
        this.id = friendOfFavorite.getId();
        this.name = friendOfFavorite.getName();
        this.score = 1.0;
    }
}