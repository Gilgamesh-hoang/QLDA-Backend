package com.commic.v1.api.user;

import com.commic.v1.dto.responses.APIResponse;
import com.commic.v1.dto.responses.CategoryResponse;
import com.commic.v1.services.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<APIResponse<List<CategoryResponse>>> getAll() {
        List<CategoryResponse> data = categoryService.getAllCategories(Pageable.unpaged());
        return ResponseEntity.ok(
                APIResponse.<List<CategoryResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .result(data)
                        .build()
        );
    }
}
