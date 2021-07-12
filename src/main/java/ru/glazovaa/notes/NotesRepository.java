package ru.glazovaa.notes;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface NotesRepository extends CrudRepository<Notes, Long> {
    Notes findNotesByHead(String head);
    Optional<Notes> findById(long id);
    Notes deleteById(long id);
    Iterable<Notes> findAllByUsername(String username);
}
