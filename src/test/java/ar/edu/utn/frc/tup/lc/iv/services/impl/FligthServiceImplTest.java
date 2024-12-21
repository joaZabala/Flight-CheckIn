package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.AirportDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.FligthsResponseDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.SeatDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.SeatStatus;
import ar.edu.utn.frc.tup.lc.iv.entities.AirportEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.FlightEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.SeatEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.AirportRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.FligthRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.SeatRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import static org.mockito.Mockito.*;

@SpringBootTest
class FligthServiceImplTest {

    @MockBean
    private FligthRepository flightRepository;

    @MockBean
    private AirportRepository airportRepository;

    @MockBean
    private SeatRepository seatRepository;

     @SpyBean
     private FligthServiceImpl fligthService;


    @Test
    void getFligthById() {

        FlightEntity flight = new FlightEntity();
        flight.setAirport(new AirportEntity());
        flight.setId("AA");

        when(flightRepository.findById("AA")).thenReturn(Optional.of(flight));

        FligthsResponseDTO fligthsResponseDTO = fligthService.getFligthById("AA");

        assertNotNull(fligthsResponseDTO);
        assertEquals(flight.getId(), fligthsResponseDTO.getId());
    }

    @Test
    void getFligthByIdNotFound() {

        when(flightRepository.findById("AA")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> fligthService.getFligthById("AA"));
    }

    @Test
    void createFligth() {

        LocalDateTime departure = LocalDateTime.now().plusHours(7);

        SeatDTO seatDTO = new SeatDTO("A1", SeatStatus.AVAILABLE);
        SeatDTO seatDTO1 = new SeatDTO("A2", SeatStatus.AVAILABLE);

        FligthsResponseDTO fligthsRequest = new FligthsResponseDTO();
        fligthsRequest.setId("AA");
        fligthsRequest.setAircraft("aircraft");
        fligthsRequest.setDeparture(departure);
        fligthsRequest.setSeatMap(List.of(seatDTO , seatDTO1));
        fligthsRequest.setAirport(new AirportDTO());


        when(airportRepository.save(any(AirportEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(flightRepository.save(any(FlightEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(seatRepository.save(any(SeatEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        FligthsResponseDTO fligthsResponseDTO = fligthService.createFligth(fligthsRequest);

        verify(airportRepository , times(1)).save(any(AirportEntity.class));
        verify(flightRepository , times(1)).save(any(FlightEntity.class));
        verify(seatRepository , times(2)).save(any(SeatEntity.class));

        assertEquals(fligthsRequest.getId(), fligthsResponseDTO.getId());
        assertNotNull(fligthsResponseDTO.getAirport());
        assertEquals(fligthsResponseDTO.getDeparture() , departure);
    }
}