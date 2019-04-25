package com.clstephenson.homeinfo.api_v1.security;

import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User domainUser = userService.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException(email));
            return new LoggedUser(domainUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
