package com.rip.RIP_Project.controller;

import com.rip.RIP_Project.dto.NoteResponse;
import com.rip.RIP_Project.entity.CustomUser;
import com.rip.RIP_Project.entity.Note;
import com.rip.RIP_Project.service.NoteService;
import com.rip.RIP_Project.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<?> getAllNotes(HttpServletRequest request) {
        String username = request.getAttribute("username").toString();
        CustomUser user = userService.findByUsername(username);

        List<NoteResponse> notesResponse = noteService.getAllNotes(user)
                .stream().map(NoteResponse::new).toList();

        return new ResponseEntity<>(notesResponse, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getNoteById(HttpServletRequest request, @PathVariable Long id) {
        String username = request.getAttribute("username").toString();
        CustomUser user = userService.findByUsername(username);

        Note note = noteService.getNoteById(id);
        if (note == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!note.getUser().equals(user)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(new NoteResponse(note), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> createNote(HttpServletRequest request, @RequestBody Note note) {
        String username = request.getAttribute("username").toString();
        CustomUser user = userService.findByUsername(username);

        note.setUser(user);

        NoteResponse noteResponse;
        try {
            noteResponse = new NoteResponse(noteService.createNote(note));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(noteResponse, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNoteById(HttpServletRequest request, @PathVariable Long id) {
        String username = request.getAttribute("username").toString();
        CustomUser user = userService.findByUsername(username);

        Note note;
        try {
            note = noteService.getNoteByIdAndUser(id, user);
        } catch (Exception e) {
            if (e.getMessage().equals("Note not found")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            noteService.deleteNoteById(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Note deleted successfully", HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(HttpServletRequest request, @PathVariable Long id, @RequestBody Note updatedNote) {
        String username = request.getAttribute("username").toString();
        CustomUser user = userService.findByUsername(username);

        Note note;
        try {
            note = noteService.updateNote(id, user, updatedNote);
        } catch (Exception e) {
            if (e.getMessage().equals("Note not found")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new NoteResponse(note), HttpStatus.OK);
    }


    @PostMapping("/{noteId}/grantAccess/{username}")
    public ResponseEntity<Void> grantAccess(@PathVariable Long noteId, @PathVariable String username) {
        CustomUser user = userService.findByUsername(username); 
        noteService.grantAccess(noteId, username);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{noteId}/revokeAccess/{username}")
    public ResponseEntity<Void> revokeAccess(@PathVariable Long noteId, @PathVariable String username) {
        CustomUser user = userService.findByUsername(username); 
        noteService.revokeAccess(noteId, username);
        return ResponseEntity.ok().build();
    }
}
