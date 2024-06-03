package com.example.friendmatchbe.repository;

import com.example.friendmatchbe.entity.UserUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserUserRepository extends JpaRepository<UserUser, Long> {
    List<UserUser> findByFirstUserId(Long FirstUserId);
}
