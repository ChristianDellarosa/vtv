package com.vtv.appointment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.domain.AppointmentType;
import com.vtv.appointment.model.dto.AppointmentDto;
import com.vtv.appointment.service.AppointmentService;
import com.vtv.appointment.util.DateUtils;
import com.vtv.appointment.util.JsonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WebMvcTest(controllers = AppointmentController.class)
public class AppointmentControllerTest {

    private static final String BASE_PATH = "/api/v1/appointments";

    private static final String SLASH = "/";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AppointmentService appointmentService;

    @Test
    public void getById_Return_200_OK() throws Exception {
        final var id = "KPP201";


        final var responseExpected = objectMapper.readValue(
                JsonUtils.loadJson("/mock/appointment-successfully.json"), AppointmentDto.class);

        final var appointment = Appointment.builder()
                .clientEmail("chris@gmail.com")
                .dateTime(ZonedDateTime.of(LocalDate.of(2023,12,31),
                        LocalTime.of(9,30,0), DateUtils.getZoneId()))
                .carPlate("ABC346")
                .type(AppointmentType.INSPECTION)
                .build();

        when(appointmentService.getLastByCarPlate(id))
                .thenReturn(appointment);

        mockMvc.perform(get(BASE_PATH + SLASH + id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseExpected)));

        verify(appointmentService).getLastByCarPlate(id);
    }
}
