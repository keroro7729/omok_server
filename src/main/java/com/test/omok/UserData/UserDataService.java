package com.test.omok.UserData;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDataService {

    private final UserDataRepository userDataRepository;

    @Autowired
    public UserDataService(UserDataRepository userDataRepository){
        this.userDataRepository = userDataRepository;
    }

    @Transactional
    public UserData createUserData(Long id){
        UserData userData = new UserData(id);
        return userDataRepository.save(userData);
    }

    public Optional<UserData> getUserDataById(Long id){
        return userDataRepository.findById(id);
    }

    @Transactional
    public UserData updateUserData(Long id, UserData userData){
        UserData exsitingUserData = userDataRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UserData not found in id: "+id));
        exsitingUserData.setBwin(userData.getBwin());
        exsitingUserData.setBdraw(userData.getBdraw());
        exsitingUserData.setBlose(userData.getBlose());
        exsitingUserData.setWwin(userData.getWwin());
        exsitingUserData.setWdraw(userData.getWdraw());
        exsitingUserData.setWlose(userData.getWlose());
        exsitingUserData.setRating(userData.getRating());
        exsitingUserData.setMoney(userData.getMoney());
        return userDataRepository.save(exsitingUserData);
    }
}
