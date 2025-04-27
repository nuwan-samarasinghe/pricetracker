package com.pricetracker.api.services.impl;

import com.pricetracker.api.exceptions.PriceTrackerException;
import com.pricetracker.api.exceptions.PriceTrackerNotFoundException;
import com.pricetracker.api.models.ProductDetail;
import com.pricetracker.api.services.ProductDetailsFetchService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service("jsonDataFetch")
public class ProductDetailsFetchStaticJsonImpl implements ProductDetailsFetchService {
    @Override
    public ProductDetail fetchProductDetails(String url) {

        ObjectMapper mapper = new ObjectMapper();
        List<ProductDetail> productDetails = null;
        try {
            InputStream is = getClass().getResourceAsStream("/static/products.json");
            productDetails = mapper.readValue(is, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new PriceTrackerException("An error occurred while fetching data", e);
        }
        return productDetails.stream()
                .filter(product -> product.getProductUrl().equalsIgnoreCase(url))
                .findFirst()
                .orElseThrow(() -> new PriceTrackerNotFoundException("No data found for the given product url"));
    }
}
