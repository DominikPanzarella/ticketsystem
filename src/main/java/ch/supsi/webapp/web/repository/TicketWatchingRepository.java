package ch.supsi.webapp.web.repository;

import ch.supsi.webapp.web.model.Ticket;
import ch.supsi.webapp.web.model.TicketWatched;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketWatchingRepository extends JpaRepository<TicketWatched, Long>
{
    public List<TicketWatched> findAllByTicket(Ticket ticket);
    public List<TicketWatched> deleteAllByTicket(Ticket ticket);
    boolean deleteAllByTicketId(int id);

}
