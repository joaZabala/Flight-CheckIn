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
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ReservationServiceImplTest {

    @Autowired
    ModelMapper modelMapper;

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private PassengerRepository passengerRepository;

    @MockBean
    private SeatRepository seatRepository;

    @MockBean
    private FligthService fligthService;

    @SpyBean
    private ReservationServiceImpl reservationService;

    private FlightEntity flightEntity;
    private ReservationEntity reservationEntity;

    private List<PassengerEntity> passengers = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        flightEntity = new FlightEntity();
        flightEntity.setId("1");
        flightEntity.setDeparture(LocalDateTime.now().plusHours(2));

        PassengerEntity passengerEntity = new PassengerEntity();
        passengerEntity.setName("Pepe");
        passengerEntity.setSeat("1A");

        PassengerEntity passengerEntity1 = new PassengerEntity();
        passengerEntity.setName("Pepe");
        passengerEntity.setSeat("1B");

        passengers.add(passengerEntity);
        passengers.add(passengerEntity1);

        reservationEntity =  new ReservationEntity();
        reservationEntity.setPassenger(passengers);
        reservationEntity.setFlight(flightEntity);
        reservationEntity.setStatus(ReservationStatus.READY_TO_CHECK_IN);
    }

    @Test
    void getReservationById() {

        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservationEntity));
        ReservationDTO reservationDTO = reservationService.getReservationById("1");
        assertNotNull(reservationDTO);

        assertEquals(reservationEntity.getStatus() , reservationDTO.getStatus());
    }

    @Test
    void getReservationByIdNotFound() {
        when(reservationRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> reservationService.getReservationById("1"));
    }

    @Test
    void createReservation() {

        ReservationDTO reservation = new ReservationDTO();
        reservation.setStatus(ReservationStatus.READY_TO_CHECK_IN);
        reservation.setPassengers(passengers.stream().map(passengerEntity -> modelMapper.map(passengerEntity , PassengerDTO.class)).toList());
        reservation.setFlight("1");
        reservation.setId("1");

        FligthsResponseDTO fligthsResponseDTO = new FligthsResponseDTO();
        fligthsResponseDTO.setSeatMap(new ArrayList<>());
        fligthsResponseDTO.setAirport(new AirportDTO());

        SeatEntity seatEntity = new SeatEntity(1L , "1A" , SeatStatus.AVAILABLE , flightEntity);

        when(fligthService.getFligthById("1")).thenReturn(fligthsResponseDTO);

        when(seatRepository.findByFlight(any(FlightEntity.class))).thenReturn(List.of(seatEntity));
        when(passengerRepository.save(any(PassengerEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(reservationRepository.save(any(ReservationEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        ReservationDTO reservationDTO = reservationService.createReservation(reservation);
        assertNotNull(reservationDTO);

    }

    @Test
    void checkInReservation() {
        ReservationDTO reservationRequest = new ReservationDTO();
        reservationRequest.setId("1");

        SeatEntity seatEntity = new SeatEntity(1L , "1A" , SeatStatus.AVAILABLE , flightEntity);
        SeatEntity seatEntity1 = new SeatEntity(1L , "1B" , SeatStatus.AVAILABLE , flightEntity);
        List<SeatEntity> seatEntities = new ArrayList<>();
        seatEntities.add(seatEntity);
        seatEntities.add(seatEntity1);

        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservationEntity));
        when(seatRepository.findByFlight(any(FlightEntity.class))).thenReturn(seatEntities);
        when(passengerRepository.save(any(PassengerEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(reservationRepository.save(any(ReservationEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        ReservationDTO reservationDTO = reservationService.checkInReservation(reservationRequest);

        assertNotNull(reservationDTO);
        assertEquals(ReservationStatus.CHECKED_IN , reservationDTO.getStatus());
        assertEquals(reservationDTO.getFlight() , "1");
        assertEquals(reservationDTO.getPassengers().size() , 2);
    }
}