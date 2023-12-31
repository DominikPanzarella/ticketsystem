package ch.supsi.webapp.web.repository;

import ch.supsi.webapp.web.model.Status;
import ch.supsi.webapp.web.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer>
{
    List<Ticket> findAllByStatus(Status status);
}
