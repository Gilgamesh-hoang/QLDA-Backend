package com.commic.v1.services.category;

import com.commic.v1.dto.responses.CategoryResponse;
import com.commic.v1.entities.Category;
import com.commic.v1.mapper.CategoryMapper;
import com.commic.v1.repositories.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    ICategoryRepository categoryRepository;
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> getAllCategories(Pageable pageable) {
        List<Category> list = categoryRepository.findAll(pageable).getContent();
        return categoryMapper.toCategoryResponse(list);
    }
}
