package com.rip.RIP_Project.service;

import com.rip.RIP_Project.entity.CustomUser;
import com.rip.RIP_Project.entity.Note;
import com.rip.RIP_Project.repository.NoteRepository;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class NoteService {
    private final NoteRepository noteRepository;

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