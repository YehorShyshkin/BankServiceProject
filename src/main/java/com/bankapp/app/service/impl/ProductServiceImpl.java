package com.bankapp.app.service.impl;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.dto.ProductDTO;
import com.bankapp.app.mapper.ProductMapper;
import com.bankapp.app.repository.ProductRepository;
import com.bankapp.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return productMapper.toDTO(productRepository.findById(UUID.fromString(id)).orElseThrow());
    }
}
