package com.rip.RIP_Project.controller;

import com.rip.RIP_Project.model.Note;
import com.rip.RIP_Project.service.NoteService;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

  
    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }


    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable Long id) {
        return noteService.getNoteById(id);
    }


    @PostMapping
    public Note createNote(@RequestBody Note note) {
        return noteService.createNote(note);
    }


    @DeleteMapping("/{id}")
    public void deleteNoteById(@PathVariable Long id) {
        noteService.deleteNoteById(id);
    }
    
    @PutMapping("/{id}")
public Note updateNote(@PathVariable Long id, @RequestBody Note updatedNote) {
    return noteService.updateNote(id, updatedNote);
}
}