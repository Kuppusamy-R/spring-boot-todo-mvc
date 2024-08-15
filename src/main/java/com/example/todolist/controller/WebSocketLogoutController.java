package com.example.todolist.controller;

import com.example.todolist.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketLogoutController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final SessionRegistry sessionRegistry;

    @Autowired
    public WebSocketLogoutController(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public void logoutOldSession(String username) {

        sessionRegistry.getAllPrincipals().forEach(principal -> {

            if (principal instanceof CustomUserDetails userDetails) {
                if (userDetails.getUser().getUsername().equals(username)) {
                    sessionRegistry.getAllSessions(principal, false).forEach(SessionInformation::expireNow);
                    messagingTemplate.convertAndSendToUser(username, "/topic/force-logout", "logout");
                }
            }

        });
    }
}
