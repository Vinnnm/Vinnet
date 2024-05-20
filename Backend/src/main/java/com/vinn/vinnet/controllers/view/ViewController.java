package com.vinn.vinnet.controllers.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vinn.vinnet.dto.UserDto;
import com.vinn.vinnet.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequiredArgsConstructor
@RequestMapping("/vinnet")
public class ViewController {

    @Autowired
	private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/feed")
    public String goFeedPage(HttpSession session) {
    	System.err.println("hiii");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            UserDto userDto = userService.getUserByEmail(authentication.getName());
            session.setAttribute("currentUser", userDto);
        }
        return "feed";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "searchResult";
    }

    @GetMapping("/friends")
    public String friendsPage() {
        return "friends";
    }
    
    @GetMapping("/chat")
    public String chatPage() {
        return "chat";
    }
}
