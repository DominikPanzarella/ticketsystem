package ch.supsi.webapp.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@WebServlet(value = "/tickets/*")
public class TicketServlet extends HttpServlet
{
    private List<Ticket> memoryTickets = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if(req.getMethod().equalsIgnoreCase("PATCH"))
            doPatch(req,res);
        else
            super.service(req,res);
    }

    private String[] tokenizePath(HttpServletRequest req)
    {
        if(req.getPathInfo() == null)
            return new String[0];
        else
            return req.getPathInfo().split("/");
    }

    private Ticket findOne(String id)
    {
        Optional<Ticket> ticket = memoryTickets.stream().filter(t -> t.getId().equalsIgnoreCase(id)).findFirst();
        return ticket.orElse(null);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String[] paths = tokenizePath(req);
        for (String path : paths) {
            System.out.println(path);
        }
        if(paths.length == 0)            // /tickets or //tickets
            success(res,memoryTickets);
        else if(paths.length == 2) {
            Ticket ticket = findOne(paths[1]);

            if(ticket!=null)
                success(res, ticket);
            else
                notFound(res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Ticket newTicket = mapper.readValue(req.getInputStream(), Ticket.class);
        newTicket.setId(UUID.randomUUID().toString());

        memoryTickets.add(newTicket);
        created(res,newTicket);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res)
    {
        String[] paths = tokenizePath(req);
        if(paths.length==2){         // /tickets/{id}
            Ticket ticketToDelete = findOne(paths[1]);

            if(ticketToDelete==null)        // ticket is not present
                notFound(res);
            else {
                memoryTickets.remove(ticketToDelete);
                noContent(res);
            }
        }
        else
            badRequest(res);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String[] paths = tokenizePath(req);

        if(paths.length == 2)       // /tickets/{id}
        {
            Ticket ticketToUpdate = findOne(paths[1]);

            if(ticketToUpdate==null)
                notFound(res);
            else
            {
                Ticket updatedTicket = mapper.readValue(req.getInputStream(),Ticket.class);

                String newAuthor =updatedTicket.getAuthor();
                String newDescription = updatedTicket.getDescription();
                String newTitle = updatedTicket.getTitle();

                if(newAuthor.equals("") || newDescription.equals("") || newTitle.equals(""))
                    badRequest(res);
                else{
                    ticketToUpdate.setAuthor        ( updatedTicket.getAuthor()      );
                    ticketToUpdate.setDescription   ( updatedTicket.getDescription() );
                    ticketToUpdate.setTitle         ( updatedTicket.getTitle()       );

                    success(res,ticketToUpdate);
                }
            }

        }
        else {
            badRequest(res);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String[] paths = tokenizePath(req);

        if(paths.length == 2)       // /tickets/{id}
        {
            Ticket ticketToUpdate = findOne(paths[1]);

            if(ticketToUpdate==null)
                notFound(res);
            else
            {
                Ticket requestTicket    = mapper.readValue(req.getInputStream(), Ticket.class);

                String newAuthor        =   requestTicket.getAuthor();
                String newDescription   =   requestTicket.getDescription();
                String newTitle         =   requestTicket.getTitle();

                ticketToUpdate.setAuthor        (newAuthor.equals("")       ?   ticketToUpdate.getAuthor()      :   newAuthor);
                ticketToUpdate.setDescription   (newDescription.equals("")  ?   ticketToUpdate.getDescription() :   newDescription);
                ticketToUpdate.setTitle         (newTitle.equals("")        ?   ticketToUpdate.getTitle()       :   newTitle);

                success(res,ticketToUpdate);
            }

        }
        else {
            badRequest(res);
        }
    }

    // Status codes

    private void notFound(final HttpServletResponse res) { res.setStatus(HttpServletResponse.SC_NOT_FOUND); }

    private void badRequest(final HttpServletResponse res) { res.setStatus(HttpServletResponse.SC_BAD_REQUEST);}

    private void noContent(final HttpServletResponse res) { res.setStatus(HttpServletResponse.SC_NO_CONTENT);}

    private void success(HttpServletResponse res, Object obj) throws IOException {
        res.setContentType("application/json");
        mapper.writeValue(res.getWriter(), obj);
    }

    private void created(HttpServletResponse res, Object obj) throws IOException {
        res.setStatus(HttpServletResponse.SC_CREATED);
        success(res, obj);
    }
}
