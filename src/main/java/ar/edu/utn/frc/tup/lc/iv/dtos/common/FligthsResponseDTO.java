package ar.edu.utn.frc.tup.lc.iv.dtos.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FligthsResponseDTO {

    private String id;

    private String aircraft;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime departure;

    private AirportDTO airport;

    @JsonProperty("seat_map")
    private List<SeatDTO> seatMap;

}
