package com.pricetracker.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pricetracker.api.PricetrackerApplication;
import com.pricetracker.api.models.ProductTrackRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = PricetrackerApplication.class)
public class ProductPriceTrackerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Invalid Request since mandatory request data is missing")
    void processInvalidRequestDueToMissingData() throws Exception {
        ProductTrackRequest trackRequest = new ProductTrackRequest();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/track")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(trackRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Validation failed for the given parameters."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"));
    }

    @Test
    @DisplayName("Adding successfully product tracking and check the success message")
    void addProductTrackingAndCheckSuccess() throws Exception {
        ProductTrackRequest trackRequest = new ProductTrackRequest();
        trackRequest.setUrl("https://example.com/product1");
        trackRequest.setDesiredPrice(99.99);
        trackRequest.setCheckFrequency("SECONDLY");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/track")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(trackRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully scheduled the price track request"));
    }

    @Test
    @DisplayName("Adding a invalid product as a request")
    void addInvalidProductRequest() throws Exception {
        ProductTrackRequest trackRequest = new ProductTrackRequest();
        trackRequest.setUrl("https://example.com/123");
        trackRequest.setDesiredPrice(99.99);
        trackRequest.setCheckFrequency("SECONDLY");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/track")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(trackRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The requested product could not be found."));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}