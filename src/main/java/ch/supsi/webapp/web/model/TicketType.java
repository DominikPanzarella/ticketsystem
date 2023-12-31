package ch.supsi.webapp.web.model;

public enum TicketType
{
    TASK("task"), STORY("story"), ISSUE("issue"), BUG("bug"), INVESTIGATION("investigation");

    final String title;

    TicketType(String title){ this.title = title; }

}
