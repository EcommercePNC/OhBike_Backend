package com.example.OhBike.service.impl;

import com.example.OhBike.common.mapper.ProductCategoryMapper;
import com.example.OhBike.dto.request.ProductCategoryRequest;
import com.example.OhBike.dto.request.UpdateProductCategoryRequest;
import com.example.OhBike.dto.response.ProductCategoryResponse;
import com.example.OhBike.entity.ProductCategory;
import com.example.OhBike.exception.BusinessRuleException;
import com.example.OhBike.entity.User;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.exception.UnauthorizedOperationException;
import com.example.OhBike.repository.ProductCategoryRepository;
import com.example.OhBike.repository.ProductRepository;
import com.example.OhBike.repository.UserRepository;
import com.example.OhBike.service.ProductCategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProductCategoryMapper categoryMapper;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductCategoryResponse createCategory (ProductCategoryRequest request, String userEmail){
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!currentUser.getRole().getName().equals("ADMIN")) {
            throw new UnauthorizedOperationException("Only ADMIN can create product categories.");
        }

        if(categoryRepository.existsByNameIgnoreCase(request.getName())){
            throw new BusinessRuleException("Product name already exists");
        }
        ProductCategory category = categoryMapper.toCategoryCreate(request);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public List<ProductCategoryResponse> getAllCategories(){
        List<ProductCategory> categories = categoryRepository.findAll();
        categories = categoryRepository.findAll();

        if(categories.isEmpty()){
           throw new ResourceNotFoundException("No registered categories were found");
        }
        return categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductCategoryResponse getCategoryById (UUID id){
        ProductCategory category = categoryRepository.findById(id)
               .orElseThrow(()-> new ResourceNotFoundException("Product not found"));
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public ProductCategoryResponse updateCategory(UpdateProductCategoryRequest request, UUID id, String userEmail){
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!currentUser.getRole().getName().equals("ADMIN")) {
            throw new UnauthorizedOperationException("Only ADMIN can update product categories.");
        }

        if (request.getName() == null) {
            throw new BusinessRuleException("You must provide at least one field to update");
        }

        ProductCategory existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!existingCategory.getName().equalsIgnoreCase(request.getName()) &&
                categoryRepository.existsByNameIgnoreCase(request.getName())){
            throw new BusinessRuleException("Category name is already in use: " + request.getName());
        }
        ProductCategory categoryToUpdate = categoryMapper.toCategoryUpdate(request, id);

        return categoryMapper.toDto(categoryRepository.save(categoryToUpdate));
    }

    @Override
    @Transactional
    public ProductCategoryResponse deleteCategory(UUID id, String userEmail){
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!currentUser.getRole().getName().equals("ADMIN")) {
            throw new UnauthorizedOperationException("Only ADMIN can delete product categories.");
        }
        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if(productRepository.existsByProductCategoryId(id)) {
            throw new BusinessRuleException("Cannot delete category because it has associated products.");
        }

        ProductCategoryResponse response =  categoryMapper.toDto(category);
        categoryRepository.deleteById(id);
        return response;
    }

}
