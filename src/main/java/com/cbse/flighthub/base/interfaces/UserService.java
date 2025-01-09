package com.cbse.flighthub.base.interfaces;

import com.cbse.flighthub.base.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User saveUser(User user);
    void addPoints(User user, int points);

    User getUserbyEmail(String email);

    User getUserById(String userId);
}
