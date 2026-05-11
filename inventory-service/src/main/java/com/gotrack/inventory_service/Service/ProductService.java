package com.gotrack.inventory_service.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gotrack.inventory_service.Dto.ProductDTO;
import com.gotrack.inventory_service.Dto.ProductResponseDTO;
import com.gotrack.inventory_service.Dto.ProfileResponseDTO;
import com.gotrack.inventory_service.Dto.UpdateProductDTO;
import com.gotrack.inventory_service.Entity.Product;
import com.gotrack.inventory_service.Exception.ConflictException;
import com.gotrack.inventory_service.Exception.NotFoundException;
import com.gotrack.inventory_service.Exception.ForbiddenException;
import com.gotrack.inventory_service.Mapper.ProductMapper;
import com.gotrack.inventory_service.filter.AuthenticationDetails;
import com.gotrack.inventory_service.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserServices userServices;

    public ProductService(ProductRepository productRepository,
            ProductMapper productMapper, UserServices userServices) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.userServices = userServices;
    }

    public ProductResponseDTO createProduct(ProductDTO productDto) {
        AuthenticationDetails authDetails = new AuthenticationDetails();
        String role = authDetails.getRole();

        ProfileResponseDTO userProfile = userServices.getProfileByAccountId(authDetails.getAccountId());

        if ("MERCHANT".equals(role) && !userProfile.getId().equals(productDto.getMerchantId())) {
            throw new ForbiddenException("You can only create your own products");
        }

        if (productRepository.existsByBaseSku(productDto.getBaseSku())) {
            throw new ConflictException("baseSku already exists");
        }
        if (productRepository.existsByNameAndMerchantId(productDto.getName(), productDto.getMerchantId())) {
            throw new ConflictException("Product name already exists for this merchant");
        }
        ProfileResponseDTO merchantProfile = userServices.get(productDto.getMerchantId());
        if (merchantProfile == null || !"MERCHANT".equals(merchantProfile.getType().toString())) {
            throw new NotFoundException(
                    "Merchant with ID " + productDto.getMerchantId() + " not found or is not a merchant");
        }

        Product product = productMapper.toEntity(productDto);
        Product saved = productRepository.save(product);

        return ProductResponseDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .merchantId(saved.getMerchantId())
                .baseSku(saved.getBaseSku())
                .build();
    }

    public void updateProduct(UpdateProductDTO dto) {
        AuthenticationDetails authDetails = new AuthenticationDetails();
        String role = authDetails.getRole();
        Long merchantId = userServices.getProfileByAccountId(authDetails.getAccountId()).getId();
        if ("MERCHANT".equals(role) && !merchantId.equals(dto.getMerchantId())) {
            throw new ForbiddenException("You can only update your own products");
        }
        Product product = productRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException(
                        "Product with ID " + dto.getId() + " not found"));

        if (!product.getName().equals(dto.getName()) &&
                productRepository.existsByNameAndMerchantId(dto.getName(), product.getMerchantId())) {
            throw new ConflictException("Product name already exists for this merchant");
        }

        product.setName(dto.getName());

        productRepository.save(product);
    }

    public Page<ProductResponseDTO> getProducts(Long merchantId, Pageable pageable) {
        Page<Product> products = productRepository.findByMerchantId(merchantId, pageable)
                .orElseThrow(() -> new NotFoundException(
                        "Product with Merchant ID " + merchantId + " not found"));

        return products.map(product -> ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .merchantId(product.getMerchantId())
                .baseSku(product.getBaseSku())
                .build());
    }

    public Long getMerchantIdByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(
                        "Product with ID " + productId + " not found"));
        return product.getMerchantId();
    }
}
