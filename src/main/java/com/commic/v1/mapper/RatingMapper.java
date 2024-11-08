package com.commic.v1.mapper;

import com.commic.v1.dto.RatingDTO;
import com.commic.v1.entities.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    @Mapping(source = "chapter.id", target = "chapterId")
    @Mapping(source = "user.id", target = "userId")
    RatingDTO toDTO(Rating rating);

    @Mapping(source = "chapterId", target = "chapter.id")
    @Mapping(source = "userId", target = "user.id")
    Rating toEntity(RatingDTO ratingDTO);
}
