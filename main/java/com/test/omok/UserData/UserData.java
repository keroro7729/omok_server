package com.test.omok.UserData;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer bwin, bdraw, blose, wwin, wdraw, wlose, rating, money;

    public UserData(){}
    public UserData(Long id){
        this.id = id;
        bwin = bdraw = blose = wwin = wdraw = wlose = 0;
        rating = 0;
        money = 0;
    }
}
