package ch.supsi.webapp.web.services;

import ch.supsi.webapp.web.model.*;
import ch.supsi.webapp.web.repository.TicketRepository;
import ch.supsi.webapp.web.repository.TicketWatchingRepository;
import ch.supsi.webapp.web.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class TicketService
{
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    private final TicketWatchingRepository watchedRepository;

    @PostConstruct
    public void init()
    {
        if(userRepository.findAll().isEmpty()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode("admin");
            User admin = new User("admin","admin","admin",encodedPassword,"ROLE_ADMIN", new HashSet<>());
            userRepository.save(admin);
            encodedPassword = passwordEncoder.encode("user");
            userRepository.save(new User("user","user","user",encodedPassword,"ROLE_USER", new HashSet<>()));
        }

        if(ticketRepository.findAll().isEmpty()) {
            Ticket ticket = new Ticket();
            User user = userRepository.findById("admin").get();
            ticket.setUser(user);
            ticket.setStatus(Status.OPEN);
            ticket.setTitle("Login non funziona");
            ticket.setDescription("Da ieri sera non riesco più a loggarmi");
            ticket.setType(TicketType.BUG);
            ticket.setCreationDate(new Date());
            ticket.setEstimate(120);
            ticket.setAssignee(user);
            LocalDateTime localDateTime = LocalDateTime.of(2024, 3, 18, 14, 0);
            // Convert LocalDateTime to Instant
            Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            ticket.setDue_date(Date.from(instant));
            ticket.setTime_spent(0);
            post(ticket);
            ticket = new Ticket();
            user = userRepository.findById("user").get();
            ticket.setUser(user);
            ticket.setStatus(Status.OPEN);
            ticket.setTitle("Schermata bianca");
            ticket.setDescription("Quando apro l'applicativo ho una schermata bianca");
            ticket.setType(TicketType.BUG);
            ticket.setCreationDate(new Date());
            ticket.setDue_date(Date.from(instant));
            ticket.setEstimate(120);
            ticket.setTime_spent(0);
            ticket.setAssignee(user);
            post(ticket);
        }

    }

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, TicketWatchingRepository watchedRepository)
    {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.watchedRepository = watchedRepository;
    }

    public List<Ticket> getAll()
    {
        return ticketRepository.findAll();
    }

    public Ticket get(int id)
    {
        return ticketRepository.findById(id).orElse(null);
    }

    public Ticket post(Ticket ticket)
    {
        ticket.setStatus(Status.OPEN);
        ticket.setCreationDate(new Date());
        ticket.setTime_spent(0);
        return ticketRepository.save(ticket);
    }

    public Ticket put(Ticket ticket)
    {
        return ticketRepository.save(ticket);
    }

    public boolean exist(int id)
    {
        return ticketRepository.existsById(id);
    }

    public boolean delete(int id)
    {
        ticketRepository.deleteById(id);
        return !ticketRepository.existsById(id);
    }

    public boolean delete2(int id, String username)
    {
        List<TicketWatched> tickets = watchedRepository.findAll();
        TicketWatched toDelete =  tickets.stream().filter(ticket -> ticket.getTicket().getId()==id && ticket.getUser().getUsername().compareToIgnoreCase(username)==0).findFirst().orElse(null);
        if(toDelete==null)  return false;
        watchedRepository.deleteById(toDelete.getId());
        return !watchedRepository.existsById(toDelete.getId());
    }

    public List<User> getUsers()
    {
        return userRepository.findAll();
    }

    public User findByUsername(final String username)
    {
        return userRepository.findUserByUsername(username);
    }

    public User putUser(User user)
    {
        user.setRole("ROLE_USER");
        return userRepository.save(user);
    }


    public List<Ticket> getAllTicketsTitleContain(String query)
    {
        return ticketRepository.findAll().stream().filter(t -> t.getTitle().contains(query)
                                                                || t.getUser().getUsername().contains(query)
                                                                || t.getUser().getName().contains(query)
                                                                || t.getDescription().contains(query)
        ).toList();
    }

    public User getAuthenticatedUser()
    {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if(authentication != null && authentication.isAuthenticated())
        {
            String username =authentication.getName();
            System.out.println(username);
            return userRepository.findUserByUsername(username);
        }
        return null;
    }



    public List<TicketWatched> findAllByUser(User user)
    {
        return watchedRepository.findAll().stream().filter(t-> t.getUser().equals(user)).toList();
    }

    public List<Ticket> findAllByUserTickets(User user)
    {
        return findAllByUser(user).stream().map(TicketWatched::getTicket).distinct().toList();
    }

    public boolean isAlreadyWatching(Ticket ticket, List<TicketWatched> watching)
    {
        return watching.stream().map(TicketWatched::getTicket).toList().contains(ticket);
    }

    public TicketWatched save(Ticket toSave, User woWatch)
    {
       var whatIsWatching = findAllByUser(woWatch);
       if(isAlreadyWatching(toSave, whatIsWatching))    return null;
       return saveTicketToWatching(toSave, woWatch);

    }

    public int sizeTicketWatching(User woWatch)
    {
        var whatIsWatching = findAllByUser(woWatch);
        return whatIsWatching.size();
    }

    private TicketWatched saveTicketToWatching(Ticket toSave,User woWatch){
        TicketWatched tw = new TicketWatched();
        tw.setTicket(toSave);
        tw.setUser(woWatch);
        return watchedRepository.save(tw);
    }


    public void updateTime(int id, long timespent)
    {
        Ticket ticket = get(id);
        ticket.setTime_spent(timespent);
        put(ticket);
    }

    public List<Ticket> findAllByStatus(Status status)
    {
        return ticketRepository.findAllByStatus(status);
    }

}
