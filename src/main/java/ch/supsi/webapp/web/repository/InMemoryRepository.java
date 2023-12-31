package ch.supsi.webapp.web.repository;

import ch.supsi.webapp.web.model.Ticket;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@Repository
public class InMemoryRepository
{
    private List<Ticket> memoryTickets = new ArrayList<>();
    private Random randomGenerator = new Random();

    public List<Ticket> findAll(){ return memoryTickets; }

    public Ticket getOne(@PathVariable int id)
    {
        return findOne(id);
    }

    public Ticket create(Ticket ticket)
    {
        ticket.setId(randomGenerator.nextInt(100000));
        memoryTickets.add(ticket);
        return ticket;

    }

    public Ticket update(int id, Ticket ticket)
    {
        Ticket dbTicket = findOne(id);
        if(dbTicket == null) return null;
        memoryTickets.remove(dbTicket.getId());
        ticket.setId(id);
        memoryTickets.add(ticket);
        return ticket;
    }

    public Boolean delete(int id)
    {
        Ticket toDelete = findOne(id);
        if(toDelete==null)      return false;
        return memoryTickets.remove(toDelete);
    }

    private Ticket findOne(final int id)
    {
        Optional<Ticket> ticket = memoryTickets.stream().filter(t -> t.getId()==id).findFirst();
        return ticket.orElse(null);
    }
}
