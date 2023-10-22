package com.bankapp.app.controller;

import com.bankapp.app.dto.ProductDTO;
import com.bankapp.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return productService.getProductDTO(id);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<String> get(@PathVariable("id") String id) {
//
//        String uuidPattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
//        if (!id.matches(uuidPattern)) {
//            throw new IllegalArgumentException("ID is not a valid UUID");
//        }
//        String response = "RESPONSE: ID is a valid UUID: " + id;
//        return ResponseEntity.ok(response);
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
//        String errorMessage = "ERROR: " + ex.getMessage();
//        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(errorMessage);
//    }
}
