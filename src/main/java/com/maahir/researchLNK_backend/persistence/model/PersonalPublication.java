package com.maahir.researchLNK_backend.persistence.model;


import com.maahir.researchLNK_backend.persistence.model.enums.PublicationStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "personal_publications")
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalPublication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name="content", columnDefinition = "TEXT")
    private String content;

    @Column(name="title", columnDefinition = "TEXT")
    private String title;

    @Column(name="authors", columnDefinition = "TEXT")
    private String authors;

    @Enumerated(EnumType.STRING)
    @Column(name="publication_type")
    private PublicationStatus publicationStatus;

    @ManyToOne
    @JoinColumn(name="profile_id")
    private Profile profile;
}
