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
    long idNote =0;
    @GetMapping("/")
    public String note(Model model){
        model.addAttribute("Notes", notesRepository.findAll());
        return "index";
    }
    @GetMapping("/notes")
    public String noteGet(Model model){
        model.addAttribute("Notes",new Notes());
        model.addAttribute("AllNotes", notesRepository.findAll());
        return "notes";
    }
    @PostMapping("/notes")
    public String notePost(@ModelAttribute("note") Notes note, Model model){
        model.addAttribute("Notes",note);
        notesRepository.save(note);
        return "redirect:/";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model){
        Optional<Notes> noteEntity = notesRepository.findById(id);
        idNote = id;
        model.addAttribute("NoteEntity",noteEntity.get());
        model.addAttribute("Redact", new Notes());
        model.addAttribute("Notes", notesRepository.findAll());
        return "redact";
    }
    @GetMapping("/redactNote")
    public String noteRedact(Model model){
        Optional<Notes> noteEntity = notesRepository.findById(idNote);
        model.addAttribute("NoteEntity",noteEntity.get());
        model.addAttribute("Notes",new Notes());
        model.addAttribute("AllNotes", notesRepository.findAll());
        return "redactNote";
    }

    @PostMapping("/redactNote")
    public String redactPost(@ModelAttribute("note") Notes note, Model model){
        Optional<Notes> noteEntity = notesRepository.findById(idNote);
        model.addAttribute("Notes",note);
        note.setHead(noteEntity.get().getHead());
        notesRepository.deleteById(idNote);
        notesRepository.save(note);
        return "redirect:/";
    }
    @GetMapping("/deleteNote")
    public String delete(Model model){
        notesRepository.deleteById(idNote);
        return "redirect:/";
    }
}
