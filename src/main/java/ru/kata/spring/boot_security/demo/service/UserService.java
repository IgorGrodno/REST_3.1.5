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
    public User getByeMail(String userName) {
        return userRepository.getByeMail(userName);
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
        User newUser = new User(user.getEMail(), user.getPassword(),user.getFirstName(),user.getLastName(), user.getAge());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRoles(user.getRoles());
        userRepository.save(newUser);
    }
    @Transactional
    public void editUser(User user) {
        User userToEdit = getById(user.getId());
        userToEdit.setEMail(user.getEMail());
        userToEdit.setAge(user.getAge());
        userToEdit.setLastName(user.getLastName());
        userToEdit.setFirstName(user.getFirstName());
        if (!(passwordEncoder.encode(user.getPassword()).equals(userToEdit.getPassword()))||(user.getPassword().length()>0)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userToEdit.setRoles(user.getRoles());
        userRepository.save(userToEdit);
    }

    @Override
    @Transactional
    @ReadOnlyProperty
    public User loadUserByUsername(String usereMail) throws UsernameNotFoundException {
        User user = getByeMail(usereMail);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", usereMail));
        }
        return user;
    }

    private Collection<? extends GrantedAuthority> MapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }



}
