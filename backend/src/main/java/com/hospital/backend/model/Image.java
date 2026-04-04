package com.hospital.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Blob;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;
    private String fileName;
    private String fileType;
    private String dwnUrl;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @JsonIgnore
    private Blob image;

    @JsonIgnore
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "pTaj", referencedColumnName = "taj"),
            @JoinColumn(name = "pRoleId", referencedColumnName = "role_id")
    })
    private Patient patient;
}
