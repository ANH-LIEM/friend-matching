package com.example.friendmatchbe.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name ="add_friend_request")
@Entity
public class AddFriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstUserIp;
    private Long firstUserId;
    private Long secondUserId;
}
