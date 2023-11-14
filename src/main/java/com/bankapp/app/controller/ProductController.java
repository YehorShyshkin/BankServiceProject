package com.bankapp.app.controller;

import com.bankapp.app.dto.ProductDTO;
import com.bankapp.app.entity.Product;
import com.bankapp.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/all")
    public List<ProductDTO> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductDTO getProductDTO(@PathVariable("id") String id) {
        String uuidPattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        if (!id.matches(uuidPattern)) {
            throw new IllegalArgumentException("ID is not a valid UUID");
        }
        return productService.getProductDTO(id);
    }

    @PostMapping("/create_product")
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        productService.save(product);
        return ResponseEntity.ok("Product was create! Success!");
    }

    @DeleteMapping("delete_product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID id) {
        boolean deleteProduct = productService.deleteProduct(id);
        if (deleteProduct) {
            return new ResponseEntity<>("Product deleted success!", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Product not found!", HttpStatus.NOT_FOUND);
        }
    }


}
