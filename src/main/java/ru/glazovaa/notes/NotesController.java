package ru.glazovaa.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class NotesController {

    @Autowired
    NotesRepository notesRepository;

    Notes note;
    Customer customer;
    CustomerRepository customerRepository;

    @GetMapping("/")
    public String note(Model model){
        model.addAttribute("Notes", notesRepository.findAll());
        return "index";
    }
    @GetMapping("/notes")
    public String noteGet(Model model){
        model.addAttribute("Notes",new Notes());
        return "notes";
    }
    @PostMapping("/notes")
    public String notePost(@ModelAttribute("note") Notes note, Model model){
        model.addAttribute("Notes",note);
        System.out.println(note.getBody());
        notesRepository.save(note);
        return "notes";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model){
        Optional<Notes> noteEntity = notesRepository.findById(id);
        model.addAttribute("NoteEntity",noteEntity.get());
        model.addAttribute("Redact", new Notes());
        model.addAttribute("Notes", notesRepository.findAll());
        return "redact";
    }
    @PostMapping("/{id}")
    public String redactPost(@ModelAttribute("note") Notes note, Model model){
        model.addAttribute("Notes",note);
        notesRepository.save(note);
        return "notes";
    }

}
