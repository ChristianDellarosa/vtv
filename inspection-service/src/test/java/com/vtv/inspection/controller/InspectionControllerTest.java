package com.vtv.inspection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vtv.inspection.model.domain.AppointmentType;
import com.vtv.inspection.model.domain.Inspection;
import com.vtv.inspection.model.domain.InspectionRequest;
import com.vtv.inspection.service.InspectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WebMvcTest(controllers = InspectionController.class)
public class InspectionControllerTest {

    private static final String BASE_PATH = "/api/v1/inspections";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InspectionService inspectionService;

    @Test
    public void inspect_Return_201_Created() throws Exception {
        final var carPlate = "KSD293";
        final var authToken = "token";
        final var inspectionRequest = InspectionRequest.builder()
                        .carPlate(carPlate)
                                .type(AppointmentType.INSPECTION)
                                        .build();
        when(inspectionService.inspect(authToken,inspectionRequest))
                .thenReturn(Inspection.builder().build()); //TODO: Crear factory

        mockMvc.perform(post(BASE_PATH)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inspectionRequest)))
                .andExpect(status().isCreated());

        verify(inspectionService).inspect(authToken, inspectionRequest);
    }

    @Test
    public void getByCarPlate_Return_emptyList_200_Ok() throws Exception {
        final var carPlate = "KSD293";

        when(inspectionService.getByCarPlate(carPlate))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_PATH + "/" + carPlate))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(inspectionService).getByCarPlate(carPlate);
    }
}