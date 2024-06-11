package com.example.friendmatchbe.entity;

import jakarta.persistence.*;
import lombok.*;

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

    public RecommendResponse(User friendOfFavorite) {
        this.id = friendOfFavorite.getId();
        this.name = friendOfFavorite.getName();
        this.score = 1.0;
    }
}