package com.bankapp.app.service;

import com.bankapp.app.dto.ProductDTO;
import com.bankapp.app.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<ProductDTO> findAll();
    ProductDTO getProductDTO(String id);

    Product findProductById(UUID productId);

    void save(Product product);
}
