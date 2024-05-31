package com.test.omok.UserData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-data")
public class UserDataController {
    private final UserDataService userDataService;

    @Autowired
    public UserDataController(UserDataService userDataService){
        this.userDataService = userDataService;
    }
}
