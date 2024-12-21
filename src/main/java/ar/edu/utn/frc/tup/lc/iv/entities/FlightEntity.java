package ar.edu.utn.frc.tup.lc.iv.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Fligth")
public class FlightEntity {
    @Id
    @Column
    private String id;

    private String aircraft;
    private LocalDateTime departure;

    @ManyToOne
    @JoinColumn(name = "airport_id")
    private AirportEntity airport;
}
