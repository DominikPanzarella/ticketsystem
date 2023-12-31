package ch.supsi.webapp.servlet;

import java.util.UUID;

public class Ticket
{
    private String title;
    private String description;
    private String author;
    private String ticketCode;

    public Ticket()
    {
        title       = "";
        description  = "";
        author      = "";
        ticketCode = String.valueOf(UUID.randomUUID());
    }

    public Ticket(
            final String aTitle,
            final String aDescription,
            final String anAuthor
    ){
        title       = aTitle;
        description  = aDescription;
        author      = anAuthor;
        ticketCode = String.valueOf(UUID.randomUUID());
    }


    // Getter methods
    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public String getAuthor() { return author; }

    public String getId(){ return ticketCode; }

    // Setter methods

    public void setTitle(final String aTitle){ title = aTitle; }

    public void setDescription(final String aDescription){ description = aDescription; }

    public void setAuthor(final String anAuthor){ author = anAuthor; }

    public void setId(final String idCode){ ticketCode = idCode;}
}
