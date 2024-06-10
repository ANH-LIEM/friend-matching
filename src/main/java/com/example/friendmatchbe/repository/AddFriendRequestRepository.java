package com.example.friendmatchbe.repository;

import com.example.friendmatchbe.entity.AddFriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddFriendRequestRepository extends JpaRepository<AddFriendRequest, Long> {
    List<AddFriendRequest> findByFirstUserId(Long firstUserId);
    List<AddFriendRequest> findBySecondUserId(Long secondUserId);
}
