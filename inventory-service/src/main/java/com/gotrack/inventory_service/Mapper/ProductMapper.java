package com.gotrack.inventory_service.Mapper;

import com.gotrack.inventory_service.Dto.ProductDTO;
import com.gotrack.inventory_service.Entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "inventoryItems", ignore = true)
    Product toEntity(ProductDTO dto);

    ProductDTO toDto(Product product);
}
