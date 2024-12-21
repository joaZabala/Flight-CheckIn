package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.FligthsResponseDTO;
import ar.edu.utn.frc.tup.lc.iv.services.FligthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlightController.class)
class FlightControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private FligthService flightService;

        @Test
        public void testGetFlightById_Success() throws Exception {
            // Prepare mock data
            FligthsResponseDTO mockFlight = new FligthsResponseDTO();
            mockFlight.setId("1");
            mockFlight.setAircraft("Boeing 737");

            // Setup mock service behavior
            when(flightService.getFligthById("1")).thenReturn(mockFlight);

            // Perform and verify the GET request
            mockMvc.perform((MockMvcRequestBuilders.get("/flights/1")
                            .contentType(MediaType.APPLICATION_JSON)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.aircraft").value("Boeing 737"));
        }

    @Test
    public void testCreateFlight_Success() throws Exception {
        // Prepare mock data for creating a flight
        FligthsResponseDTO flightToCreate = new FligthsResponseDTO();
        flightToCreate.setId("1");
        flightToCreate.setAircraft("Boeing 737");

        // Create a mock response (which could be slightly different from the input)
        FligthsResponseDTO createdFlight = new FligthsResponseDTO();
        createdFlight.setId("1");
        createdFlight.setAircraft("Boeing 737");

        // Setup mock service behavior
        when(flightService.createFligth(flightToCreate)).thenReturn(createdFlight);

        // Perform and verify the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flightToCreate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.aircraft").value("Boeing 737"));
    }

    }