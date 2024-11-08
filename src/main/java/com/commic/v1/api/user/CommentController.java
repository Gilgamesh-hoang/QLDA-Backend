package com.commic.v1.api.user;

import com.commic.v1.dto.DataListResponse;
import com.commic.v1.dto.requests.CommentCreationRequestDTO;
import com.commic.v1.dto.responses.APIResponse;
import com.commic.v1.dto.responses.CommentOverallResponse;
import com.commic.v1.dto.responses.CommentResponse;
import com.commic.v1.exception.ErrorCode;
import com.commic.v1.services.comment.CommentGetType;
import com.commic.v1.services.comment.ICommentServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    @Autowired
    ICommentServices commentServices;

    //    Comment chapter của 1 truyện
    @PostMapping("/create")
    @ResponseBody
    public APIResponse<CommentResponse> create(@RequestBody @Valid CommentCreationRequestDTO requestDTO) {
        APIResponse<CommentResponse> apiResponse = new APIResponse<>();
        apiResponse.setCode(ErrorCode.CREATE_SUCCESS.getCode());
        apiResponse.setMessage(ErrorCode.CREATE_SUCCESS.getMessage());
        CommentResponse result = commentServices.create(requestDTO);
        apiResponse.setResult(result);
        return apiResponse;
    }

    //    Lấy ra tất cả comment của 1 chapter
//    @GetMapping("/chapter/{idChapter}")
//    public APIResponse<List<CommentDTO>> getComment(@PathVariable("idChapter") Integer idChapter) {
//        APIResponse<List<CommentDTO>> apiResponse = new APIResponse<>();
//        List<CommentDTO> comments = commentServices.getComment(idChapter);
//        apiResponse.setCode(ErrorCode.FOUND.getCode());
//        apiResponse.setMessage(ErrorCode.FOUND.getMessage());
//        apiResponse.setResult(comments);
//        return apiResponse;
//    }

    @GetMapping("/chapter")
    public APIResponse<DataListResponse<CommentResponse>> getComment(
            @RequestParam(name = "type", defaultValue = "book") String type,
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        CommentGetType commentGetType = CommentGetType.valueOf(type.toUpperCase());
        APIResponse<DataListResponse<CommentResponse>> apiResponse = new APIResponse<>();
        DataListResponse<CommentResponse> comments = commentServices.getComments(commentGetType, id, pageable);
        apiResponse.setCode(ErrorCode.FOUND.getCode());
        apiResponse.setMessage(ErrorCode.FOUND.getMessage());
        apiResponse.setResult(comments);
        return apiResponse;
    }

    @GetMapping("/user")
    public APIResponse<CommentOverallResponse> getCommentOverall(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        APIResponse<CommentOverallResponse> apiResponse = new APIResponse<>();
        CommentOverallResponse commentOverallResponse = commentServices.getCommentOverall(pageable);
        apiResponse.setCode(ErrorCode.FOUND.getCode());
        apiResponse.setMessage(ErrorCode.FOUND.getMessage());
        apiResponse.setResult(commentOverallResponse);
        return apiResponse;
    }
}
