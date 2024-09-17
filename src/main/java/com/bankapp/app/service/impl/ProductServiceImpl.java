package com.bankapp.app.service.impl;

import com.bankapp.app.dto.ProductDTO;
import com.bankapp.app.exception.ManagerNotFoundException;
import com.bankapp.app.exception.ProductNotFoundException;
import com.bankapp.app.mapper.ProductMapper;
import com.bankapp.app.model.Manager;
import com.bankapp.app.model.Product;
import com.bankapp.app.model.enums.ProductStatus;
import com.bankapp.app.repository.ManagerRepository;
import com.bankapp.app.repository.ProductRepository;
import com.bankapp.app.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ManagerRepository managerRepository;

    @Override
    @Transactional
    public Product findById(UUID productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException
                        (String.format("Product with id %s not found", productId)));
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product newProduct = productMapper.toEntity(productDTO);
        Manager manager = managerRepository.findById(UUID.fromString(productDTO.getManagerId()))
                .orElseThrow(() -> new ManagerNotFoundException("Manager with id %s not found"));
        newProduct.setManager(manager);
        log.info("New product created: {}", newProduct);
        Product savedProduct = productRepository.save(newProduct);
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductDTO findProductById(UUID productId) {
        return productMapper.toDto(findById(productId));
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(UUID productId, ProductDTO productDTO) {
        Product product = findById(productId);
        productMapper.updateManagerFromDTO(productDTO,product);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    public ProductDTO softDeleteProduct(UUID productId) {
        Product product = findById(productId);
        product.setStatus(ProductStatus.DELETED);
        productRepository.save(product);
        return productMapper.toDto(product);
    }
}
