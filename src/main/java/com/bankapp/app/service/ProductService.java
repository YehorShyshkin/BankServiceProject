package com.bankapp.app.service;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> findAll();
    ProductDTO getProductDTO(String id);

}
