package com.cbse.flighthub.user;

import com.cbse.flighthub.base.entity.User;
import com.cbse.flighthub.base.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.insert(user);
    }

    @Override
    public void addPoints(User user, int points) {
        int pointEarned = user.getPointsEarned();
        user.setPointsEarned(pointEarned + points);
        userRepository.save(user);
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
}
