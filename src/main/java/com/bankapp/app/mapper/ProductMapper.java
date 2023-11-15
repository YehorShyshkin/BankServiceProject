package com.bankapp.app.mapper;

import com.bankapp.app.dto.ProductDTO;
import com.bankapp.app.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "productName", target = "productName")
    @Mapping(source = "productStatus", target = "productStatus")
    @Mapping(source = "currencyCode", target = "currencyCode")
    @Mapping(source = "productInterestRate", target = "productInterestRate")
    @Mapping(source = "productLimit", target = "productLimit")
    @Mapping(source = "manager.managerLastName", target = "managerLastName")
    @Mapping(source = "manager.managerStatus", target = "managerStatus")
    ProductDTO toDTO(Product product);
    List<ProductDTO> toDTO(List<Product> productList);
    Product updateManagerFromDTO(ProductDTO productDTO, @MappingTarget Product product);
}
