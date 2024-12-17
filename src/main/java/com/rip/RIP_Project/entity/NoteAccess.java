package com.rip.RIP_Project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Getter
@Setter
public class NoteAccess {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "note_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Note note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private CustomUser user;
}
