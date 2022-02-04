package com.heraizen.security.repo;

import com.heraizen.security.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByVerificationCode(String verificationCode);
    User findByEmail(String email);
}
