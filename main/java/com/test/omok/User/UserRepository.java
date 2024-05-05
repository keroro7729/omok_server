package com.test.omok.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<OmokUser, Long> {
    OmokUser findByUserName(String userName);
}
