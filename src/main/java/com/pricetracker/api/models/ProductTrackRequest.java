package com.pricetracker.api.models;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductTrackRequest {
    @NotBlank(message = "Product URL must not be empty.")
    private String url;
    @NotNull(message = "Desired price must not be empty.")
    private Double desiredPrice;
    @NotBlank(message = "Check frequency must not be empty.")
    private String checkFrequency;
}
