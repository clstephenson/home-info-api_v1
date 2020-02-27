package com.clstephenson.homeinfo.security;

import com.clstephenson.homeinfo.model.User;
import com.clstephenson.homeinfo.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User domainUser = userService.findByEmail(email)
                    .orElseThrow(() -> {
                        LOGGER.info(String.format("Login Failed: Username '%s' not found", email));
                        return new UsernameNotFoundException(email);
                    });
            return new LoggedUser(domainUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
