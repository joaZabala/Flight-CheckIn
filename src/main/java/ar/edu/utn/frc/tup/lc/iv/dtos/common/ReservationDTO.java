package ar.edu.utn.frc.tup.lc.iv.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private String id;
    private ReservationStatus status;

    private String flight;
    private List<PassengerDTO> passengers;
}
