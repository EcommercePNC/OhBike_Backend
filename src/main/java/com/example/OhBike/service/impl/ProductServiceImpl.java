package com.example.OhBike.service.impl;

import com.example.OhBike.common.mapper.ProductMapper;
import com.example.OhBike.dto.request.ProductRequest;
import com.example.OhBike.dto.request.UpdateProductRequest;
import com.example.OhBike.dto.response.ProductPublicResponse;
import com.example.OhBike.dto.response.ProductResponse;
import com.example.OhBike.entity.Product;
import com.example.OhBike.entity.ProductCategory;
import com.example.OhBike.entity.User;
import com.example.OhBike.exception.BusinessRuleException;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.exception.UnauthorizedOperationException;
import com.example.OhBike.repository.ProductCategoryRepository;
import com.example.OhBike.repository.ProductRepository;
import com.example.OhBike.repository.UserRepository;
import com.example.OhBike.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductCategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    private ProductResponse processProductUpdate(UpdateProductRequest request, Product existingProduct) {
        if (request.getName() == null &&
                request.getDescription() == null &&
                request.getBasePrice() == null &&
                request.getCategoryId() == null) {
            throw new BusinessRuleException("You must provide at least one field to update");
        }

        if (request.getName() != null && !request.getName().isBlank() &&
                !existingProduct.getName().equalsIgnoreCase(request.getName())) {
            if (productRepository.existsByNameIgnoreCase(request.getName())) {
                throw new BusinessRuleException("Product name is already in use: " + request.getName());
            }
        }

        ProductCategory categoryToUpdate = null;
        if (request.getCategoryId() != null) {
            categoryToUpdate = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
        }

        Product updatedProduct = productMapper.toEntityUpdate(request, existingProduct, categoryToUpdate);
        return productMapper.toDto(productRepository.save(updatedProduct));
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request, String sellerEmail) {
        User seller = userRepository.findByEmail(sellerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with email"));

        if (productRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BusinessRuleException("Product name already exists");
        }
        ProductCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: "));

        Product product = productMapper.toEntityCreate(request, category);
        product.setSeller(seller);

        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse createProductAsAdmin(ProductRequest request)
    {
        if(request.getSellerId() == null){
            throw new BusinessRuleException("Seller id is required");
        }

        User seller = userRepository.findById(request.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + request.getSellerId()));

        if(!seller.getRole().getName().equals("SELLER")){
        throw new BusinessRuleException("User must be a seller");
    }

        ProductCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: "));

        if (productRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BusinessRuleException("Product name already exists");
        }

        Product product = productMapper.toEntityCreate(request, category);
        product.setSeller(seller);

        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return productMapper.toDto(product);
    }

    @Override
    public List<ProductPublicResponse> getAllPublicProducts(UUID categoryId) {
        List<Product> products = (categoryId != null)
                ? productRepository.findByProductCategoryId(categoryId)
                : productRepository.findAll();

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No registered products were found");
        }
        return products.stream()
                .map(productMapper::toPublicDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getAllProductsAsAdmin(UUID categoryId) {
        List<Product> products = (categoryId != null)
                ? productRepository.findByProductCategoryId(categoryId)
                : productRepository.findAll();

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No registered products were found");
        }

        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<ProductResponse> getProductsBySellerEmail(String sellerEmail) {
        User seller = userRepository.findByEmail(sellerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

        List<Product> products = productRepository.findBySellerId(seller.getId());

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No registered products were found for this seller");
        }
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsBySellerId(UUID sellerId) {

        if (!userRepository.existsById(sellerId)) {
            throw new ResourceNotFoundException("Seller not found with ID: " + sellerId);
        }

        List<Product> products = productRepository.findBySellerId(sellerId);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No registered products were found for this seller");
        }

        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(UpdateProductRequest request, UUID id, String sellerEmail) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!existingProduct.getSeller().getEmail().equals(sellerEmail)) {
            throw new UnauthorizedOperationException(" You do not have permission to update this product because you are not the owner.");
        }
        return processProductUpdate(request, existingProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProductAsAdmin(UpdateProductRequest request, UUID id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return processProductUpdate(request, existingProduct);
    }

    @Override
    @Transactional
    public ProductResponse deleteProduct(UUID id, String sellerEmail) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getSeller().getEmail().equals(sellerEmail)) {
            throw new UnauthorizedOperationException(" You do not have permission to delete this product because you are not the owner.");
        }

        ProductResponse response = productMapper.toDto(product);
        productRepository.deleteById(id);

        return response;
    }


}
