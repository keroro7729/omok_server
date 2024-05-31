package com.test.omok.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public OmokUser addUser(String userName, String passwd){
        OmokUser user = new OmokUser();
        user.setId(null);
        user.setUserName(userName);
        //user.setSalt(random number hashing);
        //user.setPasswd((passwd * hashing + salt) * hashing);
        return userRepository.save(user);
    }

    public OmokUser getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
    public Optional<OmokUser> getUserById(Long id){ return userRepository.findById(id); }

    public boolean deleteUser(Long userNum) {
        if (userRepository.existsById(userNum)) {
            userRepository.deleteById(userNum);
            return true;
        }
        return false;
    }
}
