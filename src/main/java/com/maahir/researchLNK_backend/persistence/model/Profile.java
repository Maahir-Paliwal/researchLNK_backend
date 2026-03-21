package com.maahir.researchLNK_backend.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name="summary")
    private String summary;

    @Column(name="institution")
    private String institution;

    @Column(name="profile_picture_key")
    private String profilePictureKey;

    @Column(name = "position")
    private String position;

    @OneToOne(mappedBy = "profile")
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "profile")
    private SwipeCard swipeCard;

    @OneToMany(mappedBy = "profile")
    private Set<PersonalPublication> personalPublications;

}
