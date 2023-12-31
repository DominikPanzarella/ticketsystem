package ch.supsi.webapp.web.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
@Entity @Builder
public class User
{

    @Column
    @Id
    private String username;

    @Column
    private String name;

    @Column
    private String surname;

    private String password;

    private String role;

    @ManyToMany
    private Set<Ticket> watchingTicket;

    public User(String username)
    {
        this.username = username;
    }


    public void addTicketToWatch(final Ticket toWatch)
    {
        this.watchingTicket.add(toWatch);
    }

    public Set<Ticket> getTicketWatching()
    {
        return watchingTicket;
    }

    public int ticketWatchingSize()
    {
        return watchingTicket.size();
    }

}
