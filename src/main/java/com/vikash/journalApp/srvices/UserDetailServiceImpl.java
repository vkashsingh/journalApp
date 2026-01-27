package com.vikash.journalApp.srvices;

import com.vikash.journalApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.vikash.journalApp.entity.User userByName = userRepository.findByUsername(username).orElse(null);
        if(userByName!=null) {
            return User.builder()
                    .username(userByName.getUsername())
                    .password(userByName.getPassword()).roles(userByName.getRoles().toArray(new String[0])).build();
        }throw  new UsernameNotFoundException("user not found with user name :"+ username);
    }
}
