package com.rip.RIP_Project.dto;

import com.rip.RIP_Project.entity.Note;

import lombok.Data;

@Data
public class NoteResponse {

    private Long id;
    private String title;
    private String content;

    public NoteResponse(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.content = note.getContent();
    }
}
