package ar.edu.utn.frc.tup.lc.iv.dtos.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum ReservationStatus {
    @JsonProperty("READY-TO-CHECK-IN")
    READY_TO_CHECK_IN,
    @JsonProperty("CHECKED-IN")
    CHECKED_IN,
    @JsonProperty("DUE")
    DUE
}
