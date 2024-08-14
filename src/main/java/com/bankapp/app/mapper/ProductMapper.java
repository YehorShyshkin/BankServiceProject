package com.bankapp.app.mapper;

import com.bankapp.app.dto.ProductDTO;
import com.bankapp.app.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product product);
    List<ProductDTO> toDTO(List<Product> productList);
    Product updateManagerFromDTO(ProductDTO productDTO, @MappingTarget Product product);
}
