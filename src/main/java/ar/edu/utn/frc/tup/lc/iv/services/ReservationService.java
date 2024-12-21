package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.ReservationDTO;
import org.springframework.stereotype.Service;

@Service
public interface ReservationService {

    ReservationDTO getReservationById(String id);

    ReservationDTO createReservation(ReservationDTO reservation);

    ReservationDTO checkInReservation(ReservationDTO reservation);
}
