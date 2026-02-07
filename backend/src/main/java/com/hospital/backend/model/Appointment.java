package com.hospital.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    private LocalDate day;
    private LocalTime timeSlot;
    private String symptomDescription;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "dTaj", referencedColumnName = "taj"),
            @JoinColumn(name = "dRoleId", referencedColumnName = "role_id")
    })
    private Doctor doctor;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "pTaj", referencedColumnName = "taj"),
            @JoinColumn(name = "pRoleId", referencedColumnName = "role_id")
    })
    private Patient patient;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "appointment_id")
    private List<Image> symptomImg;
}
