package com.commic.v1.mapper;

import com.commic.v1.dto.CommentDTO;
import com.commic.v1.dto.requests.CommentCreationRequestDTO;
import com.commic.v1.dto.responses.CommentCreationResponse;
import com.commic.v1.dto.responses.CommentResponse;
import com.commic.v1.entities.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "chapterId", target = "chapter.id")
    Comment toComment(CommentCreationRequestDTO comment);

    @Mapping(source = "chapter.book.name", target = "bookName")
    @Mapping(source = "chapter.name", target = "chapterName")
    CommentResponse toCommentResponseDTO(Comment comment);

    CommentCreationResponse toCommentCreationRequestDTO(Comment comment);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "chapter.id", target = "chapterId")
    CommentDTO toCommentDTOs(Comment comment);
}
