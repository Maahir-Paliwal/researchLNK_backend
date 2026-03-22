package com.maahir.researchLNK_backend.persistence.model;

import com.maahir.researchLNK_backend.persistence.model.enums.ConnectionStatus;
import com.maahir.researchLNK_backend.persistence.model.keys.ConnectionId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "connections")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Connection {

    @EmbeddedId
    private ConnectionId id;

    @Column(name = "status")
    private ConnectionStatus status;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @ManyToOne
    @MapsId("userLowId")
    @JoinColumn(name="user_low_id", nullable = false)
    private User userLow;

    @ManyToOne
    @MapsId("userHighId")
    @JoinColumn(name="user_high_id", nullable = false)
    private User userHigh;

    @PrePersist
    @PreUpdate
    public void normalizeUsers(){
        if (userLow.getId().equals(userHigh.getId())) throw new IllegalArgumentException("Cannot connect a user to himself");
        if (userLow.getId() > userHigh.getId()) {
            User temp = userLow;
            userLow = userHigh;
            userHigh = temp;
        }
        id = new ConnectionId(userLow.getId(), userHigh.getId());

        if (requester != null) {
            Long requesterId = requester.getId();
            if (!(requesterId.equals(userLow.getId()) || requesterId.equals(userHigh.getId()))){
                throw new IllegalArgumentException("Requester must be one of the two users in the connection");
            }
        }

    }
}
