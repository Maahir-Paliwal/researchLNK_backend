package com.maahir.researchLNK_backend.persistence.model.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ConnectionId implements Serializable {
    @Column(name="user_low_id")
    private Long userLowId;

    @Column(name="user_high_id")
    private Long userHighId;
}
