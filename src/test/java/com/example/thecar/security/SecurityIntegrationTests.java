package com.example.thecar.security;

import com.example.thecar.model.User;
import com.example.thecar.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTests {

    private static final String USERNAME = "jully";
    private static final String PASSWORD = "secret123";

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;

    @BeforeEach
    void cleanUsers() {
        userRepository.deleteAll();
    }

    private String credentials(String username, String password) {
        return "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
    }

    private void register() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials(USERNAME, PASSWORD)))
                .andExpect(status().isCreated());
    }

    private String loginAndGetToken() throws Exception {
        String body = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials(USERNAME, PASSWORD)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(body).get("token").asText();
    }

    @Test
    void registerIsPublicAndStoresEncodedPassword() throws Exception {
        register();

        User saved = userRepository.findByUsername(USERNAME).orElseThrow();
        assertThat(saved.getPassword()).isNotEqualTo(PASSWORD);
        assertThat(saved.getRole()).isEqualTo("USER");
    }

    @Test
    void registerIgnoresRoleSentInBody() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"hacker\",\"password\":\"x\",\"role\":\"ADMIN\"}"))
                .andExpect(status().isCreated());

        assertThat(userRepository.findByUsername("hacker").orElseThrow().getRole()).isEqualTo("USER");
    }

    @Test
    void registerRejectsDuplicateUsername() throws Exception {
        register();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials(USERNAME, "other")))
                .andExpect(status().isConflict());
    }

    @Test
    void loginReturnsToken() throws Exception {
        register();

        assertThat(loginAndGetToken()).isNotBlank();
    }

    @Test
    void loginWithWrongPasswordReturnsUnauthorized() throws Exception {
        register();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials(USERNAME, "wrong")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginWithUnknownUserReturnsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials("ghost", PASSWORD)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void carsApiRequiresToken() throws Exception {
        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void carsApiRejectsInvalidToken() throws Exception {
        mockMvc.perform(get("/api/cars").header("Authorization", "Bearer invalid.token.value"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void carsApiAcceptsTokenFromLogin() throws Exception {
        register();
        String token = loginAndGetToken();

        mockMvc.perform(post("/api/cars")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"marca\":\"Fiat\",\"modelo\":\"Uno\",\"ano\":2020}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo").value("Uno"));

        mockMvc.perform(get("/api/cars").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marca").value("Fiat"));
    }

    @Test
    void loginAndRegisterPagesArePublic() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("login"));
        mockMvc.perform(get("/register")).andExpect(status().isOk()).andExpect(view().name("register"));
    }

    @Test
    void swaggerIsPublic() throws Exception {
        mockMvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
        mockMvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
    }

    @Test
    void webPagesStillRequireAuthentication() throws Exception {
        mockMvc.perform(get("/cars").accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
