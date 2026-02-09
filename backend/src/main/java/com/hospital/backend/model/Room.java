package com.hospital.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private int floor;
    private int roomNumber;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bed> beds;

    public void addBedToRoom(Bed bed) {
        this.beds.add(bed);
        bed.setRoom(this);
    }
    public void removeBedFromRoom(Bed bed) {
        this.beds.remove(bed);
        bed.setRoom(null);
    }

}
