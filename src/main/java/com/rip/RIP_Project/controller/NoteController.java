package com.rip.RIP_Project.controller;

import com.rip.RIP_Project.dto.AccessRequest;
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
import java.util.stream.Stream;

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

        List<NoteResponse> userNotes = noteService.getAllNotes(user)
                .stream().map(NoteResponse::new).toList();

        List<NoteResponse> sharedNotes = noteService.getNotesWithAccess(user)
                .stream().map(NoteResponse::new).toList();

        List<NoteResponse> allNotes = Stream.concat(userNotes.stream(), sharedNotes.stream())
                .distinct() // Убираем дубликаты (если есть)
                .toList();

        return new ResponseEntity<>(allNotes, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getNoteById(HttpServletRequest request, @PathVariable Long id) {
        String username = request.getAttribute("username").toString();
        CustomUser user = userService.findByUsername(username);

        Note note = noteService.getNoteById(id);
        if (note == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        boolean isOwner = note.getUser().equals(user);
        boolean hasAccess = noteService.hasAccess(note, user);

        if (!isOwner && !hasAccess) {
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


    @PostMapping("/{noteId}/grantAccess")
    public ResponseEntity<Void> grantAccess(HttpServletRequest request, @PathVariable Long noteId, @RequestBody AccessRequest accessRequest ) {
        String currentUsername = (String) request.getAttribute("username");
        CustomUser currentUser = userService.findByUsername(currentUsername);
 
        Note note = noteService.getNoteById(noteId);
        if (!note.getUser().equals(currentUser)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String targetUser =  accessRequest.getUsername();
        if (targetUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Целевой пользователь не найден
        }
        if (currentUsername.equals(accessRequest.getUsername())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Некорректный запрос
        }

        noteService.grantAccess(noteId, targetUser);
        return ResponseEntity.ok().build();
}


    @PostMapping("/{noteId}/revokeAccess")
    public ResponseEntity<Void> revokeAccess(HttpServletRequest request, @PathVariable Long noteId, @RequestBody AccessRequest accessRequest) {
        String currentUsername = (String) request.getAttribute("username");
        CustomUser currentUser = userService.findByUsername(currentUsername);

        Note note = noteService.getNoteById(noteId);
        if (!note.getUser().equals(currentUser)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String targetUser =  accessRequest.getUsername();
        if (targetUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Целевой пользователь не найден
        }
        if (currentUsername.equals(accessRequest.getUsername())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Некорректный запрос
        }

    noteService.revokeAccess(noteId, accessRequest.getUsername());
    return ResponseEntity.ok().build();
}
}
