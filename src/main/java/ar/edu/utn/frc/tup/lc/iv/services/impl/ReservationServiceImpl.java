package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.*;
import ar.edu.utn.frc.tup.lc.iv.entities.FlightEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.PassengerEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.ReservationEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.SeatEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.PassengerRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.ReservationRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.SeatRepository;
import ar.edu.utn.frc.tup.lc.iv.services.FligthService;
import ar.edu.utn.frc.tup.lc.iv.services.ReservationService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private FligthService fligthService;

    @Override
    public ReservationDTO getReservationById(String id) {
        Optional<ReservationEntity> reservation = reservationRepository.findById(id);

        if(reservation.isPresent()) {
            return new ReservationDTO( reservation.get().getId(),
                    reservation.get().getStatus(),
                    reservation.get().getFlight().getId(),
                    reservation.get().getPassenger().stream()
                            .map(passengerEntity -> modelMapper.map(passengerEntity , PassengerDTO.class)).toList()
            );
        }else{
            throw new EntityNotFoundException("Reservation not found");
        }
    }

    @Override
    public ReservationDTO createReservation(ReservationDTO reservation) {

       ReservationDTO reservationDTO = new ReservationDTO();
       FligthsResponseDTO flight = fligthService.getFligthById(reservation.getFlight());

       ReservationEntity reservationEntity = modelMapper.map(reservation, ReservationEntity.class);
       reservationEntity.setFlight(modelMapper.map(flight, FlightEntity.class));

       List<SeatEntity> seatEntities = seatRepository.findByFlight(reservationEntity.getFlight());
       if(hasAvailableSeat(seatEntities).isEmpty()) {
           throw new ResponseStatusException(HttpStatusCode.valueOf(400),
                   "There are no available seats for this flight");
       }

       List<PassengerEntity> passengerEntities = new ArrayList<>();
       for (PassengerDTO passenger : reservation.getPassengers()) {
         PassengerEntity passengerEntity = new PassengerEntity();
         passengerEntity.setName(passenger.getName());
         passengerEntity.setSeat(passenger.getSeat());
         passengerEntities.add(passengerRepository.save(passengerEntity));
       }

       reservationEntity.setPassenger(passengerEntities);

       reservationEntity = reservationRepository.save(reservationEntity);

       reservationDTO.setId(reservationEntity.getId());
       reservationDTO.setStatus(reservationEntity.getStatus());
       reservationDTO.setFlight(reservationEntity.getFlight().getId());
       reservationDTO.setPassengers(passengerEntities.stream().map(passenger ->
                modelMapper.map(passenger, PassengerDTO.class)).toList());

       return reservationDTO;

    }

    @Override
    public ReservationDTO checkInReservation(ReservationDTO reservation) {

        Optional<ReservationEntity> reservationEntity = reservationRepository.findById(reservation.getId());
        if(reservationEntity.isEmpty()) {
            throw new EntityNotFoundException("Reservation not found");
        } else if (!reservationEntity.get().getStatus().equals(ReservationStatus.READY_TO_CHECK_IN)) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400),
                    "The reservation is not ready to check in");
        }

        if(reservationEntity.get().getFlight().getDeparture().isBefore(LocalDateTime.now())){
            reservationEntity.get().setStatus(ReservationStatus.DUE);
            reservationRepository.save(reservationEntity.get());
            throw new ResponseStatusException(HttpStatusCode.valueOf(400),"The flight has already started");
        }

        List<SeatEntity> availableSeats = hasAvailableSeat(seatRepository.findByFlight(reservationEntity.get().getFlight()));

        reservationEntity.get().setStatus(ReservationStatus.CHECKED_IN);
        reservationEntity.get().getPassenger().forEach(passenger -> {

            // Asignar el primer asiento disponible
            SeatEntity seatToAssign = availableSeats.remove(0); // Remueve el asiento y lo asigna
            seatToAssign.setStatus(SeatStatus.RESERVED);
            seatRepository.save(seatToAssign);

            // Actualizar el pasajero con el asiento asignado
            passenger.setSeat(seatToAssign.getSeat());
            passengerRepository.save(passenger);

        });

        ReservationEntity reservationEntitySaved = reservationRepository.save(reservationEntity.get());

        return new ReservationDTO(
                reservationEntitySaved.getId(),
                reservationEntitySaved.getStatus(),
                reservationEntitySaved.getFlight().getId(),
                reservationEntitySaved.getPassenger().stream()
                        .map(passengerEntity -> modelMapper.map(passengerEntity , PassengerDTO.class)).toList()
                );
    }

    private List<SeatEntity> hasAvailableSeat(List<SeatEntity> seatEntities) {
        List<SeatEntity> hasAvailableSeat = new ArrayList<>();
        for (SeatEntity seatEntity : seatEntities) {
            if(seatEntity.getStatus().equals(SeatStatus.AVAILABLE)){
                hasAvailableSeat.add(seatEntity);
         }
        }
        return hasAvailableSeat;
    }

}
