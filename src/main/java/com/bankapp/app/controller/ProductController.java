package com.bankapp.app.controller;

import com.bankapp.app.dto.ProductDTO;
import com.bankapp.app.model.Product;
import com.bankapp.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @PostMapping("/update_product/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @RequestBody ProductDTO productDTO) {
        Product updateProduct = productService.updateProduct(id, productDTO);
        if (updateProduct != null) {
            return new ResponseEntity<>(updateProduct, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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

    @PostMapping("/product_manager/")
    public ResponseEntity<String> mergeProductAndManager(@RequestBody Map<String,UUID> request){
        UUID productId = request.get("productId");
        UUID managerId = request.get("managerId");
        if (managerId == null || productId == null){
            return new ResponseEntity<>("Invalid request. ProductId or manager Id are required!", HttpStatus.BAD_REQUEST);
        }

        boolean merge = productService.mergeProductAndManager(managerId,productId);
        if (merge){
            return new ResponseEntity<>("Product has been successfully added to manager!", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Product has not been successfully added to manager!", HttpStatus.BAD_REQUEST);
        }
    }
}
