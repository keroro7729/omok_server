package com.test.omok.GameData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameDataService {

    private final GameDataRepository gameDataRepository;

    @Autowired
    public GameDataService(GameDataRepository gameDataRepository){
        this.gameDataRepository = gameDataRepository;
    }
}
