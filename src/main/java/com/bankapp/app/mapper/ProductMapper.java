package com.bankapp.app.mapper;

import com.bankapp.app.dto.ProductDTO;
import com.bankapp.app.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "managerId", target = "manager.id")
    Product toEntity(ProductDTO productDTO);

    @Mapping(source = "manager.id", target = "managerId")
    ProductDTO toDto(Product product);

    void updateManagerFromDTO(ProductDTO productDTO, @MappingTarget Product product);
}
