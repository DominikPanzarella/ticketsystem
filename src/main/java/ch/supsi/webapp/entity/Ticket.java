package ch.supsi.webapp.entity;

public class Ticket
{
    private String title;
    private String description;
    private String author;
    private int ticketCode;
    private static int nextCode = 1;

    public Ticket()
    {
        title       = "";
        description  = "";
        author      = "";
        setNextCode();
    }

    public Ticket(
            final String aTitle,
            final String aDescription,
            final String anAuthor
    ){
        title       = aTitle;
        description  = aDescription;
        author      = anAuthor;
        setNextCode();
    }

    private void setNextCode()
    {
        ticketCode = nextCode;
        nextCode++;
    }

    // Getter methods
    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public String getAuthor() { return author; }

    public int getId(){ return ticketCode; }

    // Setter methods

    public void setTitle(final String aTitle){ title = aTitle; }

    public void setDescription(final String aDescription){ description = aDescription; }

    public void setAuthor(final String anAuthor){ author = anAuthor; }
}
