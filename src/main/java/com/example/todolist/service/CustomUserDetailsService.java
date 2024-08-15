package com.example.todolist.service;

import com.example.todolist.controller.WebSocketLogoutController;
import com.example.todolist.model.User;
import com.example.todolist.repository.UserRepository;
import com.example.todolist.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebSocketLogoutController webSocketLogoutController;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }

        webSocketLogoutController.logoutOldSession(user.getUsername());

        return new CustomUserDetails(user, user.getAuthorities());
    }
}
