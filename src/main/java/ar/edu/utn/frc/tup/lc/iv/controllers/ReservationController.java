package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.FligthsResponseDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.ReservationDTO;
import ar.edu.utn.frc.tup.lc.iv.services.FligthService;
import ar.edu.utn.frc.tup.lc.iv.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @GetMapping("{id}")
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable String id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @PostMapping()
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO request) {
        return ResponseEntity.ok(reservationService.createReservation(request));
    }

    @PutMapping()
    public ResponseEntity<ReservationDTO> checkInReservation(@RequestBody ReservationDTO request) {
        return ResponseEntity.ok(reservationService.checkInReservation(request));
    }
}
