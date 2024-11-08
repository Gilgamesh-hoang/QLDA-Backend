package com.commic.v1.services.comment;

import com.commic.v1.dto.CommentDTO;
import com.commic.v1.dto.DataListResponse;
import com.commic.v1.dto.requests.CommentCreationRequestDTO;
import com.commic.v1.dto.responses.CommentOverallResponse;
import com.commic.v1.dto.responses.CommentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICommentServices {
    CommentResponse create(CommentCreationRequestDTO requestDTO);

    List<CommentDTO> getComment(Integer idChapter);

    void deleteByChapterId(Integer id);

    DataListResponse<CommentResponse> getComments(Pageable pageable);

    boolean changeState(Integer commentId, Integer state);

    CommentResponse getCommentDetail(Integer commentId);

    DataListResponse<CommentResponse> getComments(CommentGetType commentGetType , Integer id, Pageable pageable);

    CommentOverallResponse getCommentOverall(Pageable pageable);

    Integer countAllComment();
}
