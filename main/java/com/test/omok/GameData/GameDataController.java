package com.test.omok.GameData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game-data")
public class GameDataController {

    private final GameDataService gameDataService;

    @Autowired
    public GameDataController(GameDataService gameDataService){
        this.gameDataService = gameDataService;
    }
}
