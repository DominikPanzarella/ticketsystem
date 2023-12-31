package ch.supsi.webapp.web.controller;

import ch.supsi.webapp.web.model.*;
import ch.supsi.webapp.web.services.TicketService;
import com.mysql.cj.Session;
import jdk.jfr.ContentType;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.List;


@Controller
public class MainController
{
    private final TicketService service;

    public MainController(TicketService service)
    {
        this.service = service;
    }

    @GetMapping("/*")
    public String list(Model model)
    {
        model.addAttribute("tickets",service.getAll());
        return "home";
    }

    @GetMapping("/ticket/all")
    public ResponseEntity<List<Ticket>> getAll()
    {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getAll());
    }

    @PostMapping("/*")
    public String list2(Model model){
        return list(model);
    }


    @GetMapping("/ticket/{id}")
    public String detail(@PathVariable int id, Model model)
    {
        checkTicketExists(id);
        model.addAttribute("ticket", service.get(id));
        return "ticket";

    }

    @GetMapping("/ticket/new")
    public String newPost(Model model,Authentication authentication)
    {

        if(!authentication.isAuthenticated())
            return "redirect: /home";

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser = service.findByUsername(user.getUsername());

        Ticket newTicket = new Ticket();
        newTicket.setUser(currentUser);

        List<User> users = service.getUsers();

        model.addAttribute("users",users);
        model.addAttribute("ticket", newTicket);
        model.addAttribute("isNew", true);
        //model.addAttribute("users", authors);
        model.addAttribute("user", currentUser);

        return "edit";
    }

    @PostMapping("/ticket/new")
    public String post(Ticket ticket, @RequestParam("file") MultipartFile file, @RequestParam("duedate") String due_date,Authentication authentication) throws IOException {

        if(!authentication.isAuthenticated())   return "redirect: /home";
        System.out.println("post("+ticket.getUser().getUsername()+")");
        System.out.println(due_date);
        setAttachment(ticket,file);
        setDate(ticket,due_date);
        System.out.println(ticket.getAssignee());
        ticket = service.post(ticket);
        System.out.println(ticket.getUser().getUsername());

        return "redirect:/ticket/"+ticket.getId();
    }


    private void setDate(Ticket ticket, String date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            ticket.setDue_date(dateFormat.parse(date));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format for due_date");
        }
    }

    private static void setAttachment(Ticket ticket, MultipartFile attachment) throws IOException
    {
        if(!attachment.isEmpty())
            ticket.setAttachment(
                    Attachment
                            .builder()
                            .bytes(attachment.getBytes())
                            .name(attachment.getOriginalFilename())
                            .contentType(attachment.getContentType())
                            .size(attachment.getSize())
                            .build()
            );
    }

    @PostMapping("/ticket/{id}/edit")
    public String put(@PathVariable int id,@ModelAttribute("ticket") Ticket updatedTicket, @RequestParam("file") MultipartFile file) throws IOException
    {
        Ticket ticket = service.get(id);
        ticket.setUser(updatedTicket.getUser());
        ticket.setTitle(updatedTicket.getTitle());
        ticket.setType(updatedTicket.getType());
        ticket.setStatus(updatedTicket.getStatus());
        ticket.setDescription(updatedTicket.getDescription());
        setAttachment(ticket, file);
        service.put(ticket);
        return "redirect:/ticket/{id}";

    }

    /*
        The problem in deleting the ticke carise when the ticket is being watched by another user thet is non the current one
     */
    @GetMapping(value = "/ticket/{id}/delete")
    public String delete(@PathVariable int id)
    {
        service.delete2(id);
        service.delete(id);
        return "redirect:/";
    }


    @GetMapping("/ticket/{id}/edit")
    public String edit(@PathVariable int id, Model model)
    {
        checkTicketExists(id);
        Ticket ticket = service.get(id);
        var authors = service.getUsers();
        model.addAttribute("ticket", ticket);
        model.addAttribute("isNew", false);
        model.addAttribute("users", authors);
        return "edit";

    }

    @GetMapping(value="/ticket/{id}/attachment")
    @ResponseBody
    public ResponseEntity<byte[]> getAttachmentBytes(@PathVariable int id)
    {
        Attachment attachment = service.get(id).getAttachment();
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(attachment.getContentType()))
                .body(attachment.getBytes());
    }

    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model)
    {
        User newUSer = new User();
        model.addAttribute("user",newUSer);
        model.addAttribute("userIsIn",null);
        return "register";
    }

    @PostMapping("/register")
    public String register(Model model, User user)
    {
        User existAlready = service.findByUsername(user.getUsername());
        if(existAlready!=null)
        {
            model.addAttribute("userIsIn", existAlready);
            return "/register";
        }


        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole("USER");
        service.putUser(user);
        return "redirect:/login";
    }

    @GetMapping("/tickets/search")
    public String searchTickets(Model model,@RequestParam String q)
    {
        var tickets = service.getAllTicketsTitleContain(q);
        model.addAttribute("tickets",tickets);
        return "home";
    }

    @GetMapping("/ticket/search")
    public ResponseEntity<List<Ticket>> searchTicket(@RequestParam String q)
    {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getAllTicketsTitleContain(q));
    }



    private void checkTicketExists(int id)
    {
        if(!service.exist(id))      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resourse not found!");
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "personalAccessDenied";
    }



    @GetMapping("/ticket/watch")
    public String getTicketWatching(Model model)
    {
        User currentUser = service.getAuthenticatedUser();
        var ticketsWatching = service.findAllByUserTickets(currentUser);

        model.addAttribute("tickets", ticketsWatching);
        System.out.println(currentUser.getName());
        System.out.println(ticketsWatching);
        return "watching";
    }

    @GetMapping("/ticket/watching")
    public ResponseEntity<List<Ticket>> getTicketWatchingList(Model model)
    {
        User currentUser = service.getAuthenticatedUser();
        var ticketsWatching = service.findAllByUserTickets(currentUser);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ticketsWatching);
    }

    @PostMapping(value="/ticket/{id}/watch")
    public ResponseEntity<Integer> addTicketToWatch(@PathVariable int id)
    {
        Ticket toWatch = service.get(id);
        User currentUser = service.getAuthenticatedUser();

        var newTW = service.save(toWatch,currentUser);

        int size = service.sizeTicketWatching(currentUser);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(size);
    }

    @GetMapping(value="/ticket/watch/size")
    public ResponseEntity<Integer> getSize()
    {
        User currentUser = service.getAuthenticatedUser();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.sizeTicketWatching(currentUser));
    }

    @GetMapping(value="/ticket/{id}/setTimeSpent")
    public String getPage(@PathVariable int id, Model model)
    {
        Ticket ticket = service.get(id);
        System.out.println(ticket);
        model.addAttribute("ticket", ticket);
        return "setTimeSpent";
    }

    @PostMapping(value="/ticket/{id}/setTimeSpent")
    public String updateTimeSpent(@PathVariable int id,@ModelAttribute("realtime") int timespent)
    {
        service.updateTime(id,timespent);
        return "redirect:/ticket/"+id;
    }

    @GetMapping(value="/ticket/board")
    public String getBoard(Model model)
    {
        var tickets = service.findAllByStatus(Status.OPEN);
        var tickets2 = service.findAllByStatus(Status.IN_PROGRESS);
        var tickets3 = service.findAllByStatus(Status.DONE);
        System.out.println(tickets);
        model.addAttribute("tickets_open", tickets);
        model.addAttribute("tickets_inp", tickets2);
        model.addAttribute("tickets_done",tickets3);
        return "board";
    }

    @GetMapping(value="/tickets/open")
    public ResponseEntity<List<Ticket>> getOpens()
    {
        var tickets = service.findAllByStatus(Status.OPEN);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(tickets);
    }


    @GetMapping(value="/tickets/inprogress")
    public ResponseEntity<List<Ticket>> getInProgress()
    {
        var tickets = service.findAllByStatus(Status.IN_PROGRESS);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(tickets);
    }

    @GetMapping(value="/tickets/done")
    public ResponseEntity<List<Ticket>> getDones()
    {
        var tickets = service.findAllByStatus(Status.DONE);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(tickets);
    }

    @GetMapping(value="/tickets/")
    public ResponseEntity<List<Ticket>> getAllTickets()
    {
        var tickets = service.getAll();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(tickets);
    }







}
