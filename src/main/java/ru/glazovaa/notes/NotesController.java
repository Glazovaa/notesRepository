package ru.glazovaa.notes;

import ch.qos.logback.core.encoder.EchoEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
public class NotesController {

    @Autowired
    NotesRepository notesRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserHello userHello = new UserHello();
    long idNote =0;

    @GetMapping("/")
    public String note(Principal principal, Model model){
        userHello.setNameuser(principal.getName());
        model.addAttribute("Hello", userHello);
        model.addAttribute("Notes", notesRepository.findAllByUsername(principal.getName()));
        model.addAttribute("Search",new Search());
        return "index";
    }
    @GetMapping("/login")
    public String login (){
        return "login";
    }

    @GetMapping("/notes")
    public String noteGet(Principal principal, Model model){
        userHello.setNameuser(principal.getName());
        model.addAttribute("Hello", userHello);
        model.addAttribute("Notes",new Notes());
        model.addAttribute("Search", new Search());
        model.addAttribute("AllNotes", notesRepository.findAllByUsername(principal.getName()));
        return "notes";
    }
    @PostMapping("/notes")
    public String notePost(@ModelAttribute("note") Notes note, Principal principal, Model model){
        userHello.setNameuser(principal.getName());
        model.addAttribute("Hello", userHello);
        model.addAttribute("Notes",note);
        note.setUsername(principal.getName());
        notesRepository.save(note);
        return "redirect:/";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model, Principal principal){
        Optional<Notes> noteEntity = notesRepository.findById(id);
        userHello.setNameuser(principal.getName());
        idNote = id;
        model.addAttribute("Hello", userHello);
        model.addAttribute("NoteEntity",noteEntity.get());
        model.addAttribute("Search", new Search());
        model.addAttribute("Redact", new Notes());
        model.addAttribute("Notes", notesRepository.findAllByUsername(principal.getName()));
        return "redact";
    }
    @GetMapping("/redactNote")
    public String noteRedact(Principal principal, Model model){
        Optional<Notes> noteEntity = notesRepository.findById(idNote);
        userHello.setNameuser(principal.getName());
        model.addAttribute("Hello", userHello);
        model.addAttribute("NoteEntity",noteEntity.get());
        model.addAttribute("Search", new Search());
        model.addAttribute("Notes",new Notes());
        model.addAttribute("AllNotes", notesRepository.findAllByUsername(principal.getName()));
        return "redactNote";
    }

    @PostMapping("/redactNote")
    public String redactPost(@ModelAttribute("note") Notes note, Model model){
        Optional<Notes> noteEntity = notesRepository.findById(idNote);
        model.addAttribute("Hello", userHello);
        model.addAttribute("Notes",note);
        note.setHead(noteEntity.get().getHead());
        note.setUsername(noteEntity.get().getUsername());
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
        model.addAttribute("Hello", userHello);
        model.addAttribute("Search", search);
        if (search.getSearchNote() == ""){
            return "/errorSearch";
        }else{
            return "redirect:/search/"+search.getSearchNote();
        }
    }

    @GetMapping("/search/{searchName}")
    public String showSearch(@PathVariable("searchName") String searchName, Model model, Principal principal) {
        userHello.setNameuser(principal.getName());
        model.addAttribute("Hello", userHello);
        System.out.println(searchName);
        if (searchName != ""){
            Notes noteEntity = notesRepository.findNotesByHead(searchName);
            if (noteEntity != null){
                if (noteEntity.getUsername() == principal.getName()){
                    idNote = noteEntity.getId();
                    model.addAttribute("Search", new Search());
                    model.addAttribute("NoteEntity",noteEntity);
                    model.addAttribute("Notes", notesRepository.findAllByUsername(principal.getName()));
                    return "/search";
                }else{
                    return "/errorSearch";
                }
            }else{
                return "/errorSearch";
            }
        }else {
            return "/errorSearch";
        }
    }
    @GetMapping("/errorSearch")
    public String error(Model model, Principal principal){
        userHello.setNameuser(principal.getName());
        model.addAttribute("Hello", userHello);
        model.addAttribute("Search", new Search());
        model.addAttribute("AllNotes", notesRepository.findAllByUsername(principal.getName()));
        return "errorSearch";
    }
    @PostMapping("/errorSearch")
    public String errorPost(Model model, Principal principal){
        userHello.setNameuser(principal.getName());
        model.addAttribute("Hello", userHello);
        model.addAttribute("Search", new Search());
        model.addAttribute("Notes", notesRepository.findAllByUsername(principal.getName()));
        return "redirect:/";
    }

    @GetMapping("/registration")
    public String formReg(Model model) {
        model.addAttribute("divError", false);
        model.addAttribute("reg",  new Registration ());
        return "registration";
    }
    @Autowired
    UserRepository repository;

    @PostMapping("/registration")
    public String registration(@ModelAttribute("reg") Registration reg,
                               Model model) {
        User user = repository.findByUsername(reg.getUsername());
        if (user == null) {
            if ((reg.getUsername().equals("") || reg.getPassword().equals("") || reg.getPasswordConfirm().equals(""))) {
                model.addAttribute("divError", true);
                model.addAttribute("error", "Все поля должны быть заполнены");
                model.addAttribute("reg", new Registration ());
                return "registration";
            }
            if (!reg.getPassword().equals(reg.getPasswordConfirm())) {
                model.addAttribute("divError", true);
                model.addAttribute("error", "Пароли не совпадают");
                model.addAttribute("reg", new Registration ());
                return "registration";
            }
            System.out.println(reg.getUsername()+" "+reg.getPassword());
            repository.save(new User(reg.getUsername(), bCryptPasswordEncoder.encode(reg.getPassword())));
        } else {
            model.addAttribute("divError", true);
            model.addAttribute("error", "Пользователь с таким именем уже существует");
            model.addAttribute("reg", new Registration());
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:http://localhost:8080/login";
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

class Registration {
    private String username;
    private String password;
    private String passwordConfirm;

    public Registration() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }


}
class UserHello{
    private String nameuser;

    public  UserHello(){}

    public String getNameuser() {
        return nameuser;
    }

    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }
}
