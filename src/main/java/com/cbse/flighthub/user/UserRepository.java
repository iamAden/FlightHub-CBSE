package com.cbse.flighthub.user;

import com.cbse.flighthub.base.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User save(User user);
    @Query("{'_id': ?0}")
    User getUserById(String id);
    @Query("{'email': ?0}")
    User getUserByEmail(String email);
}
