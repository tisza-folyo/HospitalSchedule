package com.hospital.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DoctorAssistantWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workId;
    private LocalDate workDay;
    private int uTaj;

    @ManyToOne
    @JoinColumn(name = "dTaj")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "aTaj")
    private Assistant assistant;
}
