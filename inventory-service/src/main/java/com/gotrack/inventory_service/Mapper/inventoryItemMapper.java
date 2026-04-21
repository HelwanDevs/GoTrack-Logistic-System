package com.gotrack.inventory_service.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.gotrack.inventory_service.Dto.inventoryItemDto;
import com.gotrack.inventory_service.Entity.InventoryItem;



@Mapper(componentModel = "spring")
public interface inventoryItemMapper {
    @Mapping(target = "product", ignore = true)
    
    InventoryItem toEntity(inventoryItemDto dto); 

    @Mapping(target = "productId", source = "item.product.id")
    @Mapping(target = "productName", source = "item.product.name")
    
    inventoryItemDto toDto(InventoryItem item);
}