package com.bankapp.app.service.impl;

import com.bankapp.app.dto.ProductDTO;
import com.bankapp.app.model.Product;
import com.bankapp.app.mapper.ProductMapper;
import com.bankapp.app.repository.ProductRepository;
import com.bankapp.app.service.ManagerService;
import com.bankapp.app.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
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
    private final ManagerService managerService;

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

    @Override
    public Product findProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found!"));
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public boolean deleteProduct(UUID id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Product updateProduct(UUID id, ProductDTO productDTO) {
        Product currentProduct = findProductById(id);
        Product updateProduct = productMapper.updateManagerFromDTO(productDTO, currentProduct);
        return productRepository.save(updateProduct);
    }

    @Override
    public boolean mergeProductAndManager(UUID managerId, UUID productId) {
//        Product currentProduct = findProductById(productId);
//        Manager currentManager = managerService.findManagerById(String.valueOf(managerId));
//        if (currentProduct != null && currentManager != null) {
//            currentProduct.setManager(currentManager);
//            productRepository.save(currentProduct);
//            return true;
//        }
        return false;
    }

}
