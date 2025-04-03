package com.example.newschedule.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "schedule")
@NoArgsConstructor
public class Comment extends BaseEntity{

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Column(columnDefinition = "longtext")
    private String contents;

    public Comment(String title, String contents){
        this.title = title;
        this.contents= contents;
    }
}
