package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.FligthsResponseDTO;
import ar.edu.utn.frc.tup.lc.iv.services.FligthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private FligthService fligthService;
    @GetMapping("/{id}")
    public ResponseEntity<FligthsResponseDTO> getFlights(@PathVariable String id) {
        return ResponseEntity.ok(fligthService.getFligthById(id));
    }

    @PostMapping()
    public ResponseEntity<FligthsResponseDTO> postFlight(@RequestBody FligthsResponseDTO request) {
        return ResponseEntity.ok(fligthService.createFligth(request));
    }
}
