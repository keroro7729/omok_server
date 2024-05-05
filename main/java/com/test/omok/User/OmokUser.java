package com.test.omok.User;

import com.test.omok.UserData.UserData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class OmokUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String passwd;
    private String salt;

    public OmokUser(){}
}
