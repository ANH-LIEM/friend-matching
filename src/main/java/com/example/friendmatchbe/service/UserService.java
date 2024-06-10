package com.example.friendmatchbe.service;

import com.example.friendmatchbe.entity.*;
import com.example.friendmatchbe.repository.AddFriendRequestRepository;
import com.example.friendmatchbe.repository.UserFavoriteRepository;
import com.example.friendmatchbe.repository.UserRepository;
import com.example.friendmatchbe.repository.UserUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<RecommendResponse> recommendFriends(Long userId) {
        List<RecommendResponse> recommendResponses = new ArrayList<>();
        Long countFavorite = (long) userFavoriteRepository.findAllByUserId(userId).size();
        User user = userRepository.findById(userId).orElse(null);
        List<User> friends = findAllFriends(userId);
        List<User> recommendFriends = new ArrayList<>();
        List<UserFavorite> userFavorites = userFavoriteRepository.findAllByUserId(userId);
        Map<Long, RecommendResponse> recommendResponseMap = new HashMap<>();

        for (UserFavorite userFavorite : userFavorites) {
            List<User> friendsOfFavorite = findAllByFavorite(userFavorite.getFavoriteId());
            for (User friendOfFavorite : friendsOfFavorite) {
                if (!friends.contains(friendOfFavorite) && !friendOfFavorite.equals(user)) {
                    if (recommendResponseMap.containsKey(friendOfFavorite.getId())) {
                        RecommendResponse existingResponse = recommendResponseMap.get(friendOfFavorite.getId());
                        existingResponse.setScore(existingResponse.getScore() + 1);
                    } else {
                        RecommendResponse newResponse = new RecommendResponse(friendOfFavorite.getId(), friendOfFavorite.getName(), friendOfFavorite.getAge(), friendOfFavorite.getUrl(), 1);
                        recommendResponseMap.put(friendOfFavorite.getId(), newResponse);
                    }
                }
            }
        }

        recommendResponses.addAll(recommendResponseMap.values());

        for (RecommendResponse recommendResponse : recommendResponses) {
            recommendResponse.setScore(100*recommendResponse.getScore() / countFavorite);
        }
        return recommendResponses;
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

    public UserUser acceptRequest(Long addFriendRequestId){
        AddFriendRequest addFriendRequest = addFriendRequestRepository.findById(addFriendRequestId).orElse(null);
        assert addFriendRequest != null;
        UserUser userUser = new UserUser();
        userUser.setFirstUserId(addFriendRequest.getSecondUserId());
        userUser.setSecondUserId(addFriendRequest.getFirstUserId());
        userUserRepository.save(userUser);
        return userUserRepository.save(convertToUserUser(addFriendRequest));
    }

    private UserUser convertToUserUser(AddFriendRequest addFriendRequest){
        UserUser userUser = new UserUser();
        userUser.setFirstUserId(addFriendRequest.getFirstUserId());
        userUser.setSecondUserId(addFriendRequest.getSecondUserId());
        return userUser;
    }
}
