package com.bankapp.app.controller;

import com.bankapp.app.dto.ProductDTO;
import com.bankapp.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/all")
    public List<ProductDTO> findAll(){
        return productService.findAll();
    }
    @GetMapping("/{id}")
    public ProductDTO getProductDTO(@PathVariable("id") String id){
        return productService.getProductDTO(id);
    }
}
