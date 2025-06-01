package com.myproject.simpleonlineshop.secutiry.user;

import com.myproject.simpleonlineshop.exception.ResourceNotFoundException;
import com.myproject.simpleonlineshop.model.User;
import com.myproject.simpleonlineshop.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // first we check if user exists by email
        //User user = Optional.ofNullable(userRepository.findByEmail(email)).orElseThrow(() -> new UsernameNotFoundException("No Such User Found in Database"));
        User user =  userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No Such User Found in Database"));
        // if exits we create a userDetails from
        MyUserDetails userDetails = MyUserDetails.buildUserDetails(user);
        return userDetails;
    }
}
