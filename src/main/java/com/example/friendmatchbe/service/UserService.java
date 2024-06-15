package com.example.friendmatchbe.service;

import com.example.friendmatchbe.entity.*;
import com.example.friendmatchbe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private FavoriteRepository favoriteRepository;

    public User save(String userIp) {
        User newUser = new User();
        newUser.setName(userIp);
        newUser.setUserIp(userIp);
        return userRepository.save(newUser);
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
        long countFavorite = userFavoriteRepository.findAllByUserId(userId).size();
        User user = userRepository.findById(userId).orElse(null);
        List<User> friends = findAllFriends(userId);
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
                        RecommendResponse newResponse = new RecommendResponse(friendOfFavorite);
                        recommendResponseMap.put(friendOfFavorite.getId(), newResponse);
                    }
                }
            }
        }

        // Bổ sung thông tin về sở thích chung và sở thích khác cho mỗi RecommendResponse
        for (Map.Entry<Long, RecommendResponse> entry : recommendResponseMap.entrySet()) {
            RecommendResponse recommendResponse = entry.getValue();
            User friendOfFavorite = userRepository.findById(recommendResponse.getId()).orElse(null);
            if (friendOfFavorite != null) {
                List<UserFavorite> friendFavorites = userFavoriteRepository.findAllByUserId(friendOfFavorite.getId());
                List<Favorite> favoritesOverlap = new ArrayList<>();
                List<Favorite> favoritesOther = new ArrayList<>();

                for (UserFavorite friendFavorite : friendFavorites) {
                    Favorite favorite = favoriteRepository.findById(friendFavorite.getFavoriteId()).orElse(null);
                    if (favorite != null) {
                        if (userFavorites.stream().anyMatch(uf -> uf.getFavoriteId().equals(friendFavorite.getFavoriteId()))) {
                            favoritesOverlap.add(favorite);
                        } else {
                            favoritesOther.add(favorite);
                        }
                    }
                }

                recommendResponse.setFavoritesOverlap(favoritesOverlap);
                recommendResponse.setFavoritesOther(favoritesOther);
            }
        }

        recommendResponses.addAll(recommendResponseMap.values());

        for (RecommendResponse recommendResponse : recommendResponses) {
            recommendResponse.setScore((int) (100 * recommendResponse.getScore() / countFavorite));
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

    public List<RecommendResponse> findAllFriendsNew(Long userId) {
        // Lấy danh sách các mối quan hệ bạn bè
        List<UserUser> listUserUser = userUserRepository.findByFirstUserId(userId);
        List<Long> friendIds = new ArrayList<>();
        for (UserUser userUser : listUserUser) {
            friendIds.add(userUser.getSecondUserId());
        }

        // Lấy danh sách sở thích của người dùng hiện tại
        List<UserFavorite> userFavorites = userFavoriteRepository.findAllByUserId(userId);
        List<Long> userFavoriteIds = userFavorites.stream().map(UserFavorite::getFavoriteId).toList();

        // Khởi tạo danh sách để chứa kết quả
        List<RecommendResponse> recommendResponses = new ArrayList<>();

        for (Long friendUserId : friendIds) {
            // Lấy thông tin bạn bè
            User friendUser = userRepository.findById(friendUserId).orElse(null);
            if (friendUser != null) {
                // Lấy danh sách sở thích của bạn bè
                List<UserFavorite> friendFavorites = userFavoriteRepository.findAllByUserId(friendUserId);

                List<Favorite> favoritesOverlap = new ArrayList<>();
                List<Favorite> favoritesOther = new ArrayList<>();

                // Phân loại sở thích chung và sở thích khác
                for (UserFavorite friendFavorite : friendFavorites) {
                    Favorite favorite = favoriteRepository.findById(friendFavorite.getFavoriteId()).orElse(null);
                    if (favorite != null) {
                        if (userFavoriteIds.contains(friendFavorite.getFavoriteId())) {
                            favoritesOverlap.add(favorite);
                        } else {
                            favoritesOther.add(favorite);
                        }
                    }
                }

                // Tạo đối tượng RecommendResponse
                RecommendResponse recommendResponse = new RecommendResponse(friendUser);
                recommendResponse.setFavoritesOverlap(favoritesOverlap);
                recommendResponse.setFavoritesOther(favoritesOther);
                recommendResponses.add(recommendResponse);
            }
        }

        // Tính toán điểm số cho từng RecommendResponse (nếu cần thiết)
        long countFavorite = userFavorites.size();
        for (RecommendResponse recommendResponse : recommendResponses) {
            recommendResponse.setScore((int) (100 * recommendResponse.getFavoritesOverlap().size() / countFavorite));
        }

        return recommendResponses;
    }


    public AddFriendRequest addFriendRequest(AddFriendRequest addFriendRequest) {
        return addFriendRequestRepository.save(addFriendRequest);
    }

    public List<RecommendResponse> findAllAddFriendRequest(Long userId) {
        // Lấy danh sách các lời mời kết bạn mà người dùng đã nhận
        List<AddFriendRequest> addFriendRequests = addFriendRequestRepository.findBySecondUserId(userId);
        List<Long> userIds = new ArrayList<>();
        for (AddFriendRequest addFriendRequest : addFriendRequests) {
            userIds.add(addFriendRequest.getFirstUserId());
        }

        // Lấy danh sách sở thích của người dùng hiện tại
        List<UserFavorite> userFavorites = userFavoriteRepository.findAllByUserId(userId);
        List<Long> userFavoriteIds = userFavorites.stream().map(UserFavorite::getFavoriteId).toList();

        // Khởi tạo danh sách để chứa kết quả
        List<RecommendResponse> recommendResponses = new ArrayList<>();

        for (Long friendUserId : userIds) {
            // Lấy thông tin người dùng gửi lời mời kết bạn
            User friendUser = userRepository.findById(friendUserId).orElse(null);
            if (friendUser != null) {
                // Lấy danh sách sở thích của người dùng gửi lời mời kết bạn
                List<UserFavorite> friendFavorites = userFavoriteRepository.findAllByUserId(friendUserId);

                List<Favorite> favoritesOverlap = new ArrayList<>();
                List<Favorite> favoritesOther = new ArrayList<>();

                // Phân loại sở thích chung và sở thích khác
                for (UserFavorite friendFavorite : friendFavorites) {
                    Favorite favorite = favoriteRepository.findById(friendFavorite.getFavoriteId()).orElse(null);
                    if (favorite != null) {
                        if (userFavoriteIds.contains(friendFavorite.getFavoriteId())) {
                            favoritesOverlap.add(favorite);
                        } else {
                            favoritesOther.add(favorite);
                        }
                    }
                }

                // Tạo đối tượng RecommendResponse
                RecommendResponse recommendResponse = new RecommendResponse(friendUser);
                recommendResponse.setFavoritesOverlap(favoritesOverlap);
                recommendResponse.setFavoritesOther(favoritesOther);
                recommendResponses.add(recommendResponse);
            }
        }

        // Tính toán điểm số cho từng RecommendResponse (nếu cần thiết)
        long countFavorite = userFavorites.size();
        for (RecommendResponse recommendResponse : recommendResponses) {
            recommendResponse.setScore((int) (100 * recommendResponse.getFavoritesOverlap().size() / countFavorite));
        }

        return recommendResponses;
    }


    public UserUser acceptRequest(Long addFriendRequestId){
        AddFriendRequest addFriendRequest = addFriendRequestRepository.findById(addFriendRequestId).orElse(null);
        assert addFriendRequest != null;
        UserUser userUser = new UserUser();
        userUser.setFirstUserId(addFriendRequest.getSecondUserId());
        userUser.setSecondUserId(addFriendRequest.getFirstUserId());
        userUserRepository.save(userUser);
        addFriendRequestRepository.delete(addFriendRequest);
        return userUserRepository.save(convertToUserUser(addFriendRequest));
    }

    private UserUser convertToUserUser(AddFriendRequest addFriendRequest){
        UserUser userUser = new UserUser();
        userUser.setFirstUserId(addFriendRequest.getFirstUserId());
        userUser.setSecondUserId(addFriendRequest.getSecondUserId());
        return userUser;
    }
}
