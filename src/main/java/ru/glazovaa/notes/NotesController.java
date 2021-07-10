package ru.glazovaa.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
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
        model.addAttribute("Search",new Search());
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("Notes", notesRepository.findAll());
        model.addAttribute("Search",new Search());
        return "login";
    }

    @GetMapping("/notes")
    public String noteGet(Model model){
        model.addAttribute("Notes",new Notes());
        model.addAttribute("Search", new Search());
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
        model.addAttribute("Search", new Search());
        model.addAttribute("Redact", new Notes());
        model.addAttribute("Notes", notesRepository.findAll());
        return "redact";
    }
    @GetMapping("/redactNote")
    public String noteRedact(Model model){
        Optional<Notes> noteEntity = notesRepository.findById(idNote);
        model.addAttribute("NoteEntity",noteEntity.get());
        model.addAttribute("Search", new Search());
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
    public String delete(){
        notesRepository.deleteById(idNote);
        return "redirect:/";
    }

    @PostMapping("/search")
    public String sendSearch(@ModelAttribute("SearchEntity") Search search , Model model){
        model.addAttribute("Search", search);
        System.out.println(search.getSearchNote());
        return "redirect:/search/"+search.getSearchNote();
    }

    @GetMapping("/search/{searchName}")
    public String showSearch(@PathVariable("searchName") String searchName, Model model) {
        Notes noteEntity = notesRepository.findNotesByHead(searchName);
//        if (noteEntity != null){
            model.addAttribute("Search", new Search());
            model.addAttribute("NoteEntity",noteEntity);
            model.addAttribute("Notes", notesRepository.findAll());
            System.out.println(searchName);
            return "/search";
//        }else{
//            System.out.println("error");
//            return "/error";
//        }
    }
//    @GetMapping("/error")
//    public String error(Model model){
//        model.addAttribute("Search", new Search());
//        model.addAttribute("AllNotes", notesRepository.findAll());
//        return "error";
//    }

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration";
        }
        if (!userService.saveUser(userForm)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        return "admin";
    }

    @PostMapping("/admin")
    public String  deleteUser(@RequestParam(required = true, defaultValue = "" ) Long userId,
                              @RequestParam(required = true, defaultValue = "" ) String action,
                              Model model) {
        if (action.equals("delete")){
            userService.deleteUser(userId);
        }
        return "admin";
    }

    @GetMapping("/admin/gt/{userId}")
    public String  gtUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("allUsers", userService.usergtList(userId));
        return "admin";
    }
}
class Search{

    String searchNote;
     public Search(String searchNote){
        this.searchNote = searchNote;
    }
    public Search (){
    }

    public String getSearchNote() {
        return searchNote;
    }

    public void setSearchNote(String searchNote) {
        this.searchNote = searchNote;
    }

}