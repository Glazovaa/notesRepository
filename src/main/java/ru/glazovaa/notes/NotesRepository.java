package ru.glazovaa.notes;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NotesRepository extends CrudRepository<Notes, Long> {
    Notes findNotesByHead(String head);
    Optional<Notes> findById(long id);
}
