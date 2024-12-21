package ar.edu.utn.frc.tup.lc.iv.repositories;

import ar.edu.utn.frc.tup.lc.iv.entities.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FligthRepository extends JpaRepository<FlightEntity, String> {
}
