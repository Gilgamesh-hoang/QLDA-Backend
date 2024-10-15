package com.commic.v1.mapper;

import com.commic.v1.dto.responses.CategoryResponse;
import com.commic.v1.entities.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

     List<CategoryResponse> toCategoryResponse(List<Category> categories);

}
