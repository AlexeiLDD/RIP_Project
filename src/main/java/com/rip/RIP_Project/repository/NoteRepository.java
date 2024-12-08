package com.rip.RIP_Project.repository;

import com.rip.RIP_Project.entity.CustomUser;
import com.rip.RIP_Project.entity.Note;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findNotesByUser(CustomUser user);
    Optional<Note> findNoteByIdAndUser(Long id, CustomUser user);
}
