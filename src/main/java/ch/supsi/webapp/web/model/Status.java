package ch.supsi.webapp.web.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

public enum Status
{
    OPEN("open"), IN_PROGRESS("in progress"), DONE("done");

    @Id
    final public String title;

    Status(String title){ this.title = title;}

}
