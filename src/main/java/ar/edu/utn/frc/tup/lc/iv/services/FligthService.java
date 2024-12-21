package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.FligthsResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface FligthService {

    FligthsResponseDTO getFligthById(String id);

    FligthsResponseDTO createFligth(FligthsResponseDTO fligthsResponseDTO);
}
