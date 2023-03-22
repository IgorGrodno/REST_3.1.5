package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/editUser/{id}")
    public String getAdminRedactor(Model user, Model roles, @PathVariable("id") Long id) {
        roles.addAttribute("roles", roleService.getRoles());
        user.addAttribute("user", userService.getById(id));
        return "editUser";
    }

    @PostMapping("/editUser")
    public String patchAdminRedactor(@ModelAttribute("user") User user) {
        userService.editUser(user);
        return "redirect:users";
    }

    @GetMapping("/deleteUser/{id}")
    public String adminDelete(@PathVariable("id") Long id) {
        userService.deleteUser((id));
        return "redirect:/admin/users";
    }

    @GetMapping("/addUser")
    public String registrationGet(@ModelAttribute("user") User user, Model roles) {
        roles.addAttribute("roles", roleService.getRoles());
        return "addUser";
    }

    @PostMapping("/addUser")
    public String registrationPost(@ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:users";
    }
}
