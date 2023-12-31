package ch.supsi.webapp.web;

import ch.supsi.webapp.web.model.User;
import ch.supsi.webapp.web.repository.TicketRepository;
import ch.supsi.webapp.web.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}


}
