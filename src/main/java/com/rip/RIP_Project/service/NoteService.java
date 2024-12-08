package com.rip.RIP_Project.service;

import com.rip.RIP_Project.model.Note;
import com.rip.RIP_Project.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    // Получить все заметки
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    // Получить заметку по ID
    public Note getNoteById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    // Создать новую заметку
    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    // Удалить заметку по ID
    public void deleteNoteById(Long id) {
        noteRepository.deleteById(id);
    }

    public Note updateNote(Long id, Note updatedNote) {
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        existingNote.setTitle(updatedNote.getTitle());
        existingNote.setContent(updatedNote.getContent());
        return noteRepository.save(existingNote);
    }
}