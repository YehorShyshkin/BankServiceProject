package com.bankapp.app.controller;

import com.bankapp.app.dto.ProductDTO;
import com.bankapp.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/creates")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        log.info("Creating product {}", productDTO);
        return productService.createProduct(productDTO);
    }

    @GetMapping("/find/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO getProductDTO(@PathVariable UUID productId) {
        return productService.findProductById(productId);
    }

    @GetMapping("/update/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO updateProduct(@PathVariable UUID productId,
                                    @RequestBody ProductDTO productDTO) {
        log.info("Updating product {}", productDTO);
        return productService.updateProduct(productId, productDTO);
    }

    @GetMapping("/delete/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO softDeleteProduct(@PathVariable UUID productId) {
        log.info("Deleting product {}", productId);
        return productService.softDeleteProduct(productId);
    }
}
