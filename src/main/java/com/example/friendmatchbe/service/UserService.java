package com.example.friendmatchbe.service;

import com.example.friendmatchbe.entity.User;
import com.example.friendmatchbe.entity.UserFavorite;
import com.example.friendmatchbe.repository.UserFavoriteRepository;
import com.example.friendmatchbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserFavoriteRepository userFavoriteRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllByFavorite(Long favoriteId) {
        List<UserFavorite> listUserFavorite = userFavoriteRepository.findAllByFavoriteId(favoriteId);
        List<User> users = new ArrayList<>();
        for (UserFavorite userFavorite : listUserFavorite) {
            users.add(userRepository.findById(userFavorite.getUserId()).orElse(null));
        }
        return users;
    }

}
