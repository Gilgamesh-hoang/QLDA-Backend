package com.commic.v1.mapper;

import com.commic.v1.dto.responses.ChapterContentResponse;
import com.commic.v1.entities.ChapterContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChapterMapper {
    @Mapping(source = "chapter.id", target = "chapterId")
    ChapterContentResponse convertToDTO(ChapterContent chapter);
}
