package com.example.newschedule.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
public class User extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @Setter
    private String mail;

    @Setter
    @Column(nullable = false)
    private String password;

    public User(String name, String userMail, String password) {
        this.name = name;
        this.mail = userMail;
        this.password = password;
    }
}
