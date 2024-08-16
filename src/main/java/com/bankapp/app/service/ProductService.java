package com.bankapp.app.service;

import com.bankapp.app.dto.ProductDTO;
import com.bankapp.app.model.Product;

import java.util.UUID;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO findProductById(UUID productId);
    Product findById(UUID productId);
    ProductDTO updateProduct(UUID productId, ProductDTO productDTO);
    ProductDTO softDeleteProduct(UUID productId);
}
