package com.commic.testCategory;


import com.commic.v1.dto.responses.CategoryResponse;
import com.commic.v1.repositories.ICategoryRepository;
import com.commic.v1.services.category.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest  // Khởi động toàn bộ context của Spring Boot
@Transactional
public class CategoryServiceTest {
    @Autowired
    ICategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    @Test
    void getAllCategories_ShouldReturnCategoriesFromDatabase() {
        List<CategoryResponse> result = categoryService.getAllCategories(Pageable.unpaged());

        // Assert: Kiểm tra kết quả trả về từ CSDL
        assertEquals(10, result.size());
        assertEquals("Tưởng tượng", result.get(0).getName());
        assertEquals("Tự lực", result.get(9).getName());
    }
}