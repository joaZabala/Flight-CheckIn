package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.AirportDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.FligthsResponseDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.SeatDTO;
import ar.edu.utn.frc.tup.lc.iv.entities.AirportEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.FlightEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.SeatEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.AirportRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.FligthRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.SeatRepository;
import ar.edu.utn.frc.tup.lc.iv.services.FligthService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FligthServiceImpl implements FligthService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FligthRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Override
    public FligthsResponseDTO getFligthById(String id) {

        Optional<FlightEntity> flight = flightRepository.findById(id);

        if(flight.isPresent()) {
            return modelMapper.map(flight.get(), FligthsResponseDTO.class);
        }else{
            throw new EntityNotFoundException("Flight not found");
        }
    }

    @Override
    @Transactional
    public FligthsResponseDTO createFligth(FligthsResponseDTO fligthsResponseDTO) {

        if(fligthsResponseDTO.getDeparture().isBefore(LocalDateTime.now().plusHours(6))){
            throw new IllegalArgumentException("Departure date must be at least 6 hours from now");
        }

        FligthsResponseDTO flightResponse = new FligthsResponseDTO();

        FlightEntity flight = modelMapper.map(fligthsResponseDTO, FlightEntity.class);

        AirportEntity airportEntity = new AirportEntity();
        airportEntity.setName(fligthsResponseDTO.getAirport().getName());
        airportEntity.setCode(fligthsResponseDTO.getAirport().getCode());
        airportEntity.setLocation(fligthsResponseDTO.getAirport().getLocation());

        flight.setAirport(airportEntity);

        airportEntity = airportRepository.save(airportEntity);
        flight = flightRepository.save(flight);

        List<SeatDTO> seatDTOS = new ArrayList<>();

        for (SeatDTO seat : fligthsResponseDTO.getSeatMap()) {

            SeatEntity seatEntity = new SeatEntity();
            seatEntity.setFlight(flight);
            seatEntity.setSeat(seat.getSeat());
            seatEntity.setStatus(seat.getStatus());

             seatEntity = seatRepository.save(seatEntity);

            seatDTOS.add(modelMapper.map(seatEntity, SeatDTO.class));
        }

        flightResponse.setId(flight.getId());
        flightResponse.setAircraft(flight.getAircraft());
        flightResponse.setDeparture(flight.getDeparture());
        flightResponse.setAirport(modelMapper.map(airportEntity, AirportDTO.class));
        flightResponse.setSeatMap(seatDTOS);
        return flightResponse;
    }
}
