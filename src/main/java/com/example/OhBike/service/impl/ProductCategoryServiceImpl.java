package com.example.OhBike.service.impl;

import com.example.OhBike.common.mapper.ProductCategoryMapper;
import com.example.OhBike.dto.request.ProductCategoryRequest;
import com.example.OhBike.dto.request.UpdateProductCategoryRequest;
import com.example.OhBike.dto.response.ProductCategoryResponse;
import com.example.OhBike.entity.ProductCategory;
import com.example.OhBike.exception.BusinessRuleException;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.repository.ProductCategoryRepository;
import com.example.OhBike.repository.ProductRepository;
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
    private final ProductCategoryMapper categoryMapper;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductCategoryResponse createCategory (ProductCategoryRequest request){
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
    public ProductCategoryResponse updateCategory(UpdateProductCategoryRequest request, UUID id){
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
    public ProductCategoryResponse deleteCategory(UUID id){
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
