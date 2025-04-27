package com.pricetracker.api.services;

import com.pricetracker.api.models.ProductDetail;

public interface ProductDetailsFetchService {
    ProductDetail fetchProductDetails(String url);
}
