package ch.supsi.webapp.web.model;

import ch.supsi.webapp.web.model.Ticket;
import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CreateTicketRequest
{
    private String title;
    private String description;
    private String username;

}
