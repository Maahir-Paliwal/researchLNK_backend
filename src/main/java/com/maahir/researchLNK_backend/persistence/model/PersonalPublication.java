package com.maahir.researchLNK_backend.persistence.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "personal_publications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalPublication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
}
