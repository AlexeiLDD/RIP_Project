package com.rip.RIP_Project.service;

import com.rip.RIP_Project.entity.CustomUser;
import com.rip.RIP_Project.entity.Note;
import com.rip.RIP_Project.entity.NoteAccess;
import com.rip.RIP_Project.repository.NoteAccessRepository;
import com.rip.RIP_Project.repository.NoteRepository;
import com.rip.RIP_Project.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private UserRepository userRepository;
    private NoteAccessRepository noteAccessRepository;

    public NoteService(NoteRepository noteRepository) { this.noteRepository = noteRepository; }

    // Получить все заметки
    public List<Note> getAllNotes(CustomUser user) { return noteRepository.findNotesByUser(user); }

    // Получить заметку по ID
    public Note getNoteById(Long id) { return noteRepository.findById(id).orElse(null); }

    // Получить заметку по ID и пользователю
    public Note getNoteByIdAndUser(Long id, CustomUser user) {
        return noteRepository.findNoteByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Note not found"));
    }
    // Предоставление доступа
    public void grantAccess(Long noteId, String username) {
        Optional<Note> noteOptional = noteRepository.findById(noteId);
        CustomUser user = userRepository.findByUsername(username);
            if (noteOptional.isPresent()) {
            Note note = noteOptional.get();
            
            Optional<NoteAccess> existingAccess = noteAccessRepository.findByNoteAndUser(note, user);
            if(existingAccess.isEmpty()){
                NoteAccess noteAccess = new NoteAccess();
                noteAccess.setNote(note);
                noteAccess.setUser(user);
                noteAccessRepository.save(noteAccess);
            }
        } else {
            throw new IllegalArgumentException("Note or user not found");
        }
    }
    // Отбираем доступ
    public void revokeAccess(Long noteId, String username) {
        Optional<Note> noteOptional = noteRepository.findById(noteId);
        CustomUser user = userRepository.findByUsername(username);

        if (noteOptional.isPresent()) {
            Note note = noteOptional.get();
            
            noteAccessRepository.deleteByNoteAndUser(note, user);
        } else {
            throw new IllegalArgumentException("Note or user not found");
        }
    }

    // Создать новую заметку
    public Note createNote(Note note) { return noteRepository.save(note); }

    // Удалить заметку по ID
    public void deleteNoteById(Long id) { noteRepository.deleteById(id); }

    public Note updateNote(Long id, CustomUser user, Note updatedNote) {
        Note existingNote = noteRepository.findNoteByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        existingNote.setTitle(updatedNote.getTitle());
        existingNote.setContent(updatedNote.getContent());
        return noteRepository.save(existingNote);
    }
}