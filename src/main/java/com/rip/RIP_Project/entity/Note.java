package com.rip.RIP_Project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Setter
@Getter
@Entity
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CustomUser user;

}
