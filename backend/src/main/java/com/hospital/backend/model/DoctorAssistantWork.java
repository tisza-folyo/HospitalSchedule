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
    private String uTaj;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "dTaj", referencedColumnName = "taj"),
            @JoinColumn(name = "dRoleId", referencedColumnName = "role_id")
    })
    private Doctor doctor;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "aTaj", referencedColumnName = "taj"),
            @JoinColumn(name = "aRoleId", referencedColumnName = "role_id")
    })
    private Assistant assistant;
}
