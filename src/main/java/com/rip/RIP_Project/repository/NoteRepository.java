package com.rip.RIP_Project.repository;

import com.rip.RIP_Project.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
