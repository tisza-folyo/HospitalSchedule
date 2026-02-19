package com.hospital.backend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(PersonId.class)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"taj", "role_id"})
})
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Doctor.class, name = "doctor"),
        @JsonSubTypes.Type(value = Admin.class, name = "admin"),
        @JsonSubTypes.Type(value = Patient.class, name = "patient")
})
public abstract class Person {

    @Id
    private String taj;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    @JsonIgnore
    private String password;

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
