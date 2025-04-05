package com.ecommerce.controller;

import com.ecommerce.entity.Product;
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
public class ProductControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;
    private String adminToken;

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
    }

    @Test
    public void testGetAllProductsNoAuth() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateProductAsUser() throws Exception {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(99.99);
        product.setStock(10);

        mockMvc.perform(post("/api/products")
                        .header("Authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCreateProductAsAdmin() throws Exception {
        Product product = new Product();
        product.setName("Admin Product");
        product.setDescription("Admin Description");
        product.setPrice(199.99);
        product.setStock(20);

        mockMvc.perform(post("/api/products")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateProductAsUser() throws Exception {
        Product product = new Product();
        product.setName("Updated Product");
        product.setDescription("Updated Description");
        product.setPrice(299.99);
        product.setStock(30);

        mockMvc.perform(put("/api/products/1")
                        .header("Authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateProductAsAdmin() throws Exception {
        Product product = new Product();
        product.setName("Admin Updated Product");
        product.setDescription("Admin Updated Description");
        product.setPrice(399.99);
        product.setStock(40);

        mockMvc.perform(put("/api/products/1")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteProductAsUser() throws Exception {
        mockMvc.perform(delete("/api/products/1")
                        .header("Authorization", userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteProductAsAdmin() throws Exception {
        // Note: This test might fail if product with ID 1 doesn't exist
        // In a real test, you would create a product first and then delete it
        mockMvc.perform(delete("/api/products/1")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk());
    }
} 