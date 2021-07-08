package ru.glazovaa.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Controller
public class NotesController {

    @Autowired
    NotesRepository notesRepository;

    private Notes note;
    private Customer customer;
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
        Iterable<Notes> notes = notesRepository.findAll();
        model.addAttribute("notes", notes);
        return "";
    }

    private CustomerRepository customerRepository;

//    @GetMapping("/registration")
//    public String registration(Model model) {
//        model.addAttribute("Customer", new Customer());
//        return "registration";
//    }
//
//    @PostMapping("/registration")
//    public String addUser(@ModelAttribute("note") Customer customer, Model model) {
//        model.addAttribute("Customer", customer);
//        customerRepository.save(customer);
//        return "redirect:/login";
//    }
}
