package ch.supsi.webapp.web.repository;

import ch.supsi.webapp.web.model.TicketWatched;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketWatchingRepository extends JpaRepository<TicketWatched, Long>
{
    public TicketWatched deleteById(int id);

}
