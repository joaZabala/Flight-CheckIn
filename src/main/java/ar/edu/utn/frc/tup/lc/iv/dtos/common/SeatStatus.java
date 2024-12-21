package ar.edu.utn.frc.tup.lc.iv.dtos.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SeatStatus {
    @JsonProperty("reserved")
    RESERVED,
    @JsonProperty("available")

    AVAILABLE
}
