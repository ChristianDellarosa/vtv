package com.vtv.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vtv.auth.domain.SignIn;
import com.vtv.auth.service.AuthService;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest {

    private static final String BASE_PATH = "/api/v1/auth";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthService authService;

    @Test
    public void signIn_Return_200_Ok() throws Exception {

        final var base64BasicAuth = "Q2hyaXN0aWFuOnBhc3N3b3Jk";
        final var bearerToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJDaHJpc3RpYW4wMSIsImlzcyI6ImF1dGgtc2VydmljZSIsImV4cCI6MTcwMDUxMjU2MSwiaWF0IjoxNzAwNTAwNTYxLCJqdGkiOiIwZmRjMGI4MS03M2IwLTQ4MDYtODc3MC1kZGY0NDA0ODJiNDQifQ.yBJ3ZbGI91zz8KLn72Pau19uAwNdjfMecIrFZDyTXy8";
        final var signInMock = SignIn.builder()
                .accessToken(bearerToken)
                .build();

        when(authService.signIn(base64BasicAuth))
                .thenReturn(signInMock);

        mockMvc.perform(post(BASE_PATH + "/sign-in")
                        .header(HttpHeaders.AUTHORIZATION, base64BasicAuth))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(signInMock)));

        verify(authService).signIn(base64BasicAuth);
    }

    @Test
    public void signIn_withoutAuthorizationHeader_Return_400_BadRequest() throws Exception {

        mockMvc.perform(post(BASE_PATH + "/sign-in"))
                .andExpect(status().isBadRequest());

        verify(authService, never()).signIn(anyString());
    }
}