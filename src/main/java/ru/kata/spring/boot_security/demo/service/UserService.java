package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    @ReadOnlyProperty
    public User getByUserName(String userName) {
        return userRepository.getByUserName(userName);
    }
    @Transactional
    @ReadOnlyProperty
    public User getById(long id) {
        return userRepository.getById(id);
    }
    @Transactional
    @ReadOnlyProperty
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Transactional
    public void deleteUser(long id) {
        userRepository.delete(getById(id));
    }
    @Transactional
    public void addUser(User user) {
        User newUser = new User(user.getEMail(), user.getPassword(),user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRoles(user.getRoles());
        userRepository.save(newUser);
    }
    @Transactional
    public void editUser(User user) {
        User userToEdit = getById(user.getId());
        userToEdit.setEMail(user.getEMail());
        if (!passwordEncoder.encode(user.getPassword()).equals(userToEdit.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userToEdit.setRoles(user.getRoles());
        userToEdit.setUsername(user.getUsername());
        userRepository.save(userToEdit);
    }

    @Override
    @Transactional
    @ReadOnlyProperty
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return user;
    }

    private Collection<? extends GrantedAuthority> MapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }



}
