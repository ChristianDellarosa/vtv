package com.vtv.appointment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vtv.appointment.controller.AppointmentController;
import com.vtv.appointment.model.domain.ScheduleQuery;
import com.vtv.appointment.service.schedule.ScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WebMvcTest(controllers = ScheduleController.class)
public class ScheduleControllerTest {

    private static final String BASE_PATH = "/api/v1/schedules";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScheduleService scheduleService;

    @Test
    public void get_Return_200_Ok() throws Exception {
        final var hour = 19;
        final var scheduleQuery = ScheduleQuery.builder()
                .hour(hour)
                .build();

        final List<ZonedDateTime> responseExpected = Collections.emptyList();

        when(scheduleService.get(scheduleQuery))
                .thenReturn(responseExpected);

        mockMvc.perform(get(String.format("%s?hour=%s", BASE_PATH, hour)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseExpected)));

        verify(scheduleService).get(scheduleQuery);
    }
}
