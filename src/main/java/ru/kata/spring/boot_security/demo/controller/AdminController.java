package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin/addUser")
    public String createUserForm(Model model) {
        List<Role> roles = roleService.getRoles();
        model.addAttribute("roles", roles);
        return "addUser";
    }

    @PostMapping("/admin/addUser")
    public String addUser(User user) {
        userService.addUser(user);
        return "redirect:/users";
    }

    @GetMapping("/admin/editUser/{id}")
    public String editUserForm(Model model, @PathVariable("id") long id) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        List<Role> roles = roleService.getRoles();
        model.addAttribute("roles", roles);
        return "editUser";
    }

    @PostMapping("/admin/editUser")
    public String editUser(User user) {
        userService.editUser(user);
        return "redirect:/users";
    }

    @GetMapping("/admin/deleteUser/{id}")
    public String deleteUser(Model model, @PathVariable("id") long id) {
        userService.deleteUser(id);
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
}
