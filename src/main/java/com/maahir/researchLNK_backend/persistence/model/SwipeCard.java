package com.maahir.researchLNK_backend.persistence.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "swipe_cards")
public class SwipeCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @OneToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;
}
