package com.commic.v1.services.category;

import com.commic.v1.dto.responses.CategoryResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService {
    List<CategoryResponse> getAllCategories(Pageable pageable);
}
