package com.example.friendmatchbe.service;

import com.example.friendmatchbe.entity.AddFriendRequest;
import com.example.friendmatchbe.entity.User;
import com.example.friendmatchbe.entity.UserFavorite;
import com.example.friendmatchbe.entity.UserUser;
import com.example.friendmatchbe.repository.AddFriendRequestRepository;
import com.example.friendmatchbe.repository.UserFavoriteRepository;
import com.example.friendmatchbe.repository.UserRepository;
import com.example.friendmatchbe.repository.UserUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private UserUserRepository userUserRepository;
    @Autowired
    private AddFriendRequestRepository addFriendRequestRepository;

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

    public List<User> findAllFriends(Long userId){
        List<UserUser> listUserUser = userUserRepository.findByFirstUserId(userId);
        List<User> users = new ArrayList<>();
        for (UserUser userUser : listUserUser) {
            users.add(userRepository.findById(userUser.getSecondUserId()).orElse(null));
        }
        return users;
    }

    public AddFriendRequest addFriendRequest(AddFriendRequest addFriendRequest) {
        return addFriendRequestRepository.save(addFriendRequest);
    }

    public List<AddFriendRequest> findAllAddFriendRequest(Long userId) {
        return addFriendRequestRepository.findBySecondUserId(userId);
    }

    public UserUser acceptRequest(AddFriendRequest addFriendRequest){
        return userUserRepository.save(convertToUserUser(addFriendRequest));
    }

    private UserUser convertToUserUser(AddFriendRequest addFriendRequest){
        UserUser userUser = new UserUser();
        userUser.setFirstUserId(addFriendRequest.getFirstUserId());
        userUser.setSecondUserId(addFriendRequest.getSecondUserId());
        return userUser;
    }
}
