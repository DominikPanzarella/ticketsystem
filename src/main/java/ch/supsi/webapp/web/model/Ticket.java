package ch.supsi.webapp.web.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode(of= {"id"}) @ToString
@Entity
public class Ticket
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private long estimate;

    @Column
    private long time_spent;

    @ManyToOne
    private User user;

    @ManyToOne
    private User assignee;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date due_date;



    @Enumerated(EnumType.STRING)
    private TicketType type;

    @Embedded
    private Attachment attachment;

    public boolean isTimeSpentSet(){
        return time_spent > 0;
    }


}
