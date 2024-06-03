package com.example.friendmatchbe.repository;

import com.example.friendmatchbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
