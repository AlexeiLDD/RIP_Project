package com.rip.RIP_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.rip.RIP_Project.entity.CustomUser;
import com.rip.RIP_Project.entity.Note;
import com.rip.RIP_Project.entity.NoteAccess;

public interface NoteAccessRepository extends JpaRepository<NoteAccess, Long>{
    Optional<NoteAccess> findByNoteAndUser(Note note, CustomUser user);
}
