package com.hospital.backend.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(PersonId.class)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Person {

    @Id
    private String taj;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String password;

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
