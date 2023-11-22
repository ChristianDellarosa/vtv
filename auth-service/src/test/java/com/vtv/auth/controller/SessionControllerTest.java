package com.vtv.auth.controller;

import com.vtv.auth.service.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WebMvcTest(controllers = SessionController.class)
public class SessionControllerTest {
    private static final String BASE_PATH = "/api/v1/session";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @Test
    public void validateSession_Return_200_Ok() throws Exception {

        final var bearerToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJDaHJpc3RpYW4wMSIsImlzcyI6ImF1dGgtc2VydmljZSIsImV4cCI6MTcwMDUxMjU2MSwiaWF0IjoxNzAwNTAwNTYxLCJqdGkiOiIwZmRjMGI4MS03M2IwLTQ4MDYtODc3MC1kZGY0NDA0ODJiNDQifQ.yBJ3ZbGI91zz8KLn72Pau19uAwNdjfMecIrFZDyTXy8";

        doNothing().when(sessionService).validateSession(bearerToken);

        mockMvc.perform(post(BASE_PATH)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isOk());

        verify(sessionService).validateSession(bearerToken);
    }

    @Test
    public void validateSession_withoutAuthorizationHeader_Return_400_BadRequest() throws Exception {

        mockMvc.perform(post(BASE_PATH))
                .andExpect(status().isBadRequest());

        verify(sessionService, never()).validateSession(anyString());
    }
}
