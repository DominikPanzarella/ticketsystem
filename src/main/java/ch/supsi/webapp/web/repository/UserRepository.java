package ch.supsi.webapp.web.repository;

import ch.supsi.webapp.web.model.Ticket;
import ch.supsi.webapp.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>
{
    User findUserByUsername(String username);
}
