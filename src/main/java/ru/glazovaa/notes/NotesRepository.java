package ru.glazovaa.notes;

import org.springframework.data.repository.CrudRepository;

public interface NotesRepository extends CrudRepository<Notes, Long> {
    Notes findNotesByHead(String head);
}
