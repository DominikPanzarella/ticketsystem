package ch.supsi.webapp.web.config;

import ch.supsi.webapp.web.model.User;
import ch.supsi.webapp.web.services.TicketService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService
{

    private final TicketService service;

    public CustomUserDetailService(TicketService service)
    {
        this.service = service;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Username: "+username);
        User currentUser = service.findByUsername(username);
        System.out.println("Role: "+currentUser.getRole());
        if(currentUser == null)
        {
            throw new UsernameNotFoundException("User not found");
        }

        List<GrantedAuthority> auth = AuthorityUtils.createAuthorityList(currentUser.getRole());
        return new org.springframework.security.core.userdetails.User(username, currentUser.getPassword(), true, true, true, true, auth);
    }

}
