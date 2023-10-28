package com.bankapp.app.service.impl;

import com.bankapp.app.dto.ProductDTO;
import com.bankapp.app.entity.Product;
import com.bankapp.app.mapper.ProductMapper;
import com.bankapp.app.repository.ProductRepository;
import com.bankapp.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDTO> findAll() {
        return productMapper.toDTO(productRepository.findAll());
    }

    @Override
    public ProductDTO getProductDTO(String id) {
        Optional<Product> productOptional = productRepository.findById(UUID.fromString(id));
        Product product = productOptional.orElseThrow(() -> new NoSuchElementException("Product not found!"));
        return productMapper.toDTO(product);
    }


}
