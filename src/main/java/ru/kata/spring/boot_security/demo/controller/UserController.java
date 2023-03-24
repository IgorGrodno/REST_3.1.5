package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String userPage(Model model, Principal principal) {
        model.addAttribute("curentUser",userService.getByeMail(principal.getName()));
        String adminRole = "false";
        Set<Role> curentRoles = userService.getByeMail(principal.getName()).getRoles();
        for(Role role:curentRoles){
            if (role.getName().equals("ADMIN")||role.getName().equals("ROLE_ADMIN"))
            {
                adminRole="true";
            }
        }
        model.addAttribute("adminRole",adminRole);
        return "/user";
    }

}
