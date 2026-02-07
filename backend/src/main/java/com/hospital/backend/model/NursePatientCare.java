package com.hospital.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NursePatientCare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long careId;
    private int uTaj;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "pTaj", referencedColumnName = "taj"),
            @JoinColumn(name = "pRoleId", referencedColumnName = "role_id")
    })
    private Patient patient;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "nTaj", referencedColumnName = "taj"),
            @JoinColumn(name = "nRoleId", referencedColumnName = "role_id")
    })
    private Nurse nurse;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToOne
    @JoinColumn(name = "bed_id")
    private Bed bed;
}
