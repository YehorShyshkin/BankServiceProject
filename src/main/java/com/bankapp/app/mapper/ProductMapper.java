package com.bankapp.app.mapper;

import com.bankapp.app.controller.dto.ProductDTO;
import com.bankapp.app.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "manager.managerLastName", target = "managerLastName")
    @Mapping(source = "manager.managerStatus", target = "managerStatus")
    ProductDTO toDTO(Product product);
    List<ProductDTO> toDTO(List<Product> productList);
    Product updateManagerFromDTO(ProductDTO productDTO, @MappingTarget Product product);
}
