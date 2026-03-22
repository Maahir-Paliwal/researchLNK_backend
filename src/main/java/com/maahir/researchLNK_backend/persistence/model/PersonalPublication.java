package com.maahir.researchLNK_backend.persistence.model;


import com.maahir.researchLNK_backend.persistence.model.enums.PublicationStatus;
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

    @Column(name="content")
    private String content;

    @Column(name="title")
    private String title;

    @Column(name="authors")
    private String authors;

    @Enumerated(EnumType.STRING)
    @Column(name="publication_type")
    private PublicationStatus publicationStatus;

    @ManyToOne
    @JoinColumn(name="profile_id")
    private Profile profile;
}
