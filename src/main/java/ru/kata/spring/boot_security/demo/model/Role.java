package ru.kata.spring.boot_security.demo.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    public Role() {

    }

    @Override
    public String getAuthority() {
        return name;
    }

    public Role (String name){
        this.name=name;
    }

    @Override
    public String toString(){
        return name;
    }
}
