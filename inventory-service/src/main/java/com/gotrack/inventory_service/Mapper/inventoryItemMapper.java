package com.gotrack.inventory_service.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.gotrack.inventory_service.Dto.inventoryItemDto;
import com.gotrack.inventory_service.Entity.inventoryItem;

// @Mapper(componentModel = "spring")
// public interface inventoryItemMapper {
//     @Mapping(target = "product", ignore = true)
//     inventoryItem toEntity(inventoryItemDto dto); 

//     @Mapping(target = "productId", source = "product.id")
//     @Mapping(target = "productName", source = "product.name")
//     inventoryItemDto toDto(inventoryItem item);
// }


@Mapper(componentModel = "spring")
public interface inventoryItemMapper {
    @Mapping(target = "product", ignore = true)
    // غيري 'inventoryItemDto' لـ 'dto' (حروف صغيرة ومختصرة)
    inventoryItem toEntity(inventoryItemDto dto); 

    @Mapping(target = "productId", source = "item.product.id")
    @Mapping(target = "productName", source = "item.product.name")
    // غيري 'inventoryItem' لـ 'item'
    inventoryItemDto toDto(inventoryItem item);
}