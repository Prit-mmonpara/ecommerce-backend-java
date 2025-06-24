package com.ecommerce.controller;

import com.ecommerce.payload.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;
    private String adminToken;
    private Long userId;
    private Long adminId;

    @BeforeEach
    public void setup() throws Exception {
        // Get user token
        LoginRequest userLoginRequest = new LoginRequest();
        userLoginRequest.setUsername("testuser");
        userLoginRequest.setPassword("password123");

        MvcResult userResult = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String userResponse = userResult.getResponse().getContentAsString();
        userToken = "Bearer " + objectMapper.readTree(userResponse).get("token").asText();
        userId = objectMapper.readTree(userResponse).get("id").asLong();

        // Get admin token
        LoginRequest adminLoginRequest = new LoginRequest();
        adminLoginRequest.setUsername("testadmin");
        adminLoginRequest.setPassword("admin123");

        MvcResult adminResult = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLoginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String adminResponse = adminResult.getResponse().getContentAsString();
        adminToken = "Bearer " + objectMapper.readTree(adminResponse).get("token").asText();
        adminId = objectMapper.readTree(adminResponse).get("id").asLong();
    }

    @Test
    public void testAccessCartWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAddToCartAsUser() throws Exception {
        mockMvc.perform(post("/api/cart/add")
                        .header("Authorization", userToken)
                        .param("userId", userId.toString())
                        .param("productId", "1")
                        .param("quantity", "2"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllCartsAsUser() throws Exception {
        mockMvc.perform(get("/api/cart")
                        .header("Authorization", userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetAllCartsAsAdmin() throws Exception {
        mockMvc.perform(get("/api/cart")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUserCartAsUser() throws Exception {
        mockMvc.perform(get("/api/cart/" + userId)
                        .header("Authorization", userToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOtherUserCartAsUser() throws Exception {
        // User should not be able to access another user's cart
        mockMvc.perform(get("/api/cart/" + adminId)
                        .header("Authorization", userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetUserCartAsAdmin() throws Exception {
        // Admin should be able to access any user's cart
        mockMvc.perform(get("/api/cart/" + userId)
                        .header("Authorization", adminToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateCartAsUser() throws Exception {
        mockMvc.perform(put("/api/cart/update")
                        .header("Authorization", userToken)
                        .param("userId", userId.toString())
                        .param("productId", "1")
                        .param("quantity", "3"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateOtherUserCartAsUser() throws Exception {
        // User should not be able to update another user's cart
        mockMvc.perform(put("/api/cart/update")
                        .header("Authorization", userToken)
                        .param("userId", adminId.toString())
                        .param("productId", "1")
                        .param("quantity", "3"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testRemoveFromCartAsUser() throws Exception {
        mockMvc.perform(delete("/api/cart/remove")
                        .header("Authorization", userToken)
                        .param("userId", userId.toString())
                        .param("productId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveFromOtherUserCartAsUser() throws Exception {
        // User should not be able to remove from another user's cart
        mockMvc.perform(delete("/api/cart/remove")
                        .header("Authorization", userToken)
                        .param("userId", adminId.toString())
                        .param("productId", "1"))
                .andExpect(status().isForbidden());
    }
} 