package com.commic.v1.services.comment;

import com.commic.v1.dto.CommentDTO;
import com.commic.v1.dto.DataListResponse;
import com.commic.v1.dto.requests.CommentCreationRequestDTO;
import com.commic.v1.dto.responses.CommentCreationResponse;
import com.commic.v1.dto.responses.CommentOverallResponse;
import com.commic.v1.dto.responses.CommentResponse;
import com.commic.v1.entities.Comment;
import com.commic.v1.entities.User;
import com.commic.v1.exception.AppException;
import com.commic.v1.exception.ErrorCode;
import com.commic.v1.mapper.CommentMapper;
import com.commic.v1.repositories.IBookRepository;
import com.commic.v1.repositories.IChapterRepository;
import com.commic.v1.repositories.ICommentRepository;
import com.commic.v1.repositories.IUserRepository;
import com.commic.v1.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImp implements ICommentServices {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IChapterRepository chapterRepository;
    @Autowired
    ICommentRepository commentRepository;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    IBookRepository bookRepository;

    @Override
    public CommentResponse create(CommentCreationRequestDTO requestDTO) {
        User user = SecurityUtils.getUserFromPrincipal(userRepository);
        if (!chapterRepository.existsChapterById(requestDTO.getChapterId())) {
            throw new AppException(ErrorCode.CHAPTER_NOT_FOUND);
        }
        Comment comment = commentMapper.toComment(requestDTO);
        comment.setCreatedAt(new Date(System.currentTimeMillis()));
        comment.setState(CommentConst.SHOW.getValue());
        comment.setUser(user);
        comment = commentRepository.save(comment);
        CommentCreationResponse commentDTO = commentMapper.toCommentCreationRequestDTO(comment);
        CommentResponse response = commentMapper.toCommentResponseDTO(comment);
        response.setUser(CommentResponse.UserCommentDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .build());
        return response;
    }

    @Override
    public List<CommentDTO> getComment(Integer idChapter) {
        try {
            List<Comment> comments = commentRepository.findByChapterId(idChapter);
            return comments.stream()
                    .map(commentMapper::toCommentDTOs)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public void deleteByChapterId(Integer id) {
        List<Comment> comments = commentRepository.findByChapterId(id);
        for (Comment comment : comments) {
            comment.setIsDeleted(true);
        }
        commentRepository.saveAllAndFlush(comments);
    }

    //    ADMIN
    public DataListResponse<CommentResponse> getComments(Pageable pageable) {
        DataListResponse<CommentResponse> result = new DataListResponse<>();
        Page<Comment> page = commentRepository.findAll(pageable);
        List<CommentResponse> data = page.getContent().stream().map(item -> commentMapper.toCommentResponseDTO(item)).toList();
        result.setCurrentPage(pageable.getPageNumber() + 1);
        result.setTotalPages(page.getTotalPages());
        result.setData(data);
        return result;
    }

    @Override
    public CommentResponse getCommentDetail(Integer commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) return null;
        CommentResponse commentResponse = commentMapper.toCommentResponseDTO(comment);
        CommentResponse.UserCommentDTO userCommentDTO = CommentResponse.UserCommentDTO.builder()
                .username(comment.getUser().getUsername())
                .email(comment.getUser().getEmail())
                .avatar(comment.getUser().getAvatar())
                .build();
        commentResponse.setUser(userCommentDTO);
        Optional<String> thumbnail = bookRepository.findThumbnailBookId(comment.getChapter().getBook().getId());
        commentResponse.setThumbnail(thumbnail.orElse(null));
        return commentResponse;
    }

//    @Override
//    public List<CommentDTO> getAllComment(Integer id) {
//        List<Comment> comments = commentRepository.findByBookId(id);
//        return comments.stream()
//                .map(commentMapper::toCommentDTOs)
//                .collect(Collectors.toList());
//    }

    @Override
    public boolean changeState(Integer commentId, Integer state) {
        if (CommentConst.fromValue(state) == null) return false;
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) return false;
        if (state == CommentConst.SHOW.getValue()) {
            comment.setState(CommentConst.SHOW.getValue());
        }
        if (state == 2) {
            comment.setState(CommentConst.HIDE.getValue());
        }

        commentRepository.save(comment);
        return true;
    }

    @Override
    public DataListResponse<CommentResponse> getComments(CommentGetType commentGetType, Integer id, Pageable pageable) {
        DataListResponse<CommentResponse> result = new DataListResponse<>();
        Page<Comment> page = null;
        List<CommentResponse> data;
        switch (commentGetType) {
            case BY_CHAPTER -> {
                page = commentRepository.getCommentByChapterId(id, pageable);
            }
            case BY_BOOK -> {
                page = commentRepository.getCommentByBookId(id, pageable);
            }
        }
        if (page == null || page.isEmpty()) throw new AppException(ErrorCode.NOT_FOUND);
        data = page.getContent().stream().map(comment -> {
            CommentResponse commentResponse = commentMapper.toCommentResponseDTO(comment);
            CommentResponse.UserCommentDTO userCommentDTO = CommentResponse.UserCommentDTO.builder()
                    .username(comment.getUser().getUsername())
                    .email(comment.getUser().getEmail())
                    .avatar(comment.getUser().getAvatar())
                    .build();
            commentResponse.setUser(userCommentDTO);
            Optional<String> thumbnail = bookRepository.findThumbnailBookId(comment.getChapter().getBook().getId());
            commentResponse.setThumbnail(thumbnail.orElse(null));
            return commentResponse;
        }).toList();
        result.setData(data);
        result.setCurrentPage(pageable.getPageNumber() + 1);
        result.setTotalPages(page.getTotalPages());
        return result;
    }

    @Override
    public CommentOverallResponse getCommentOverall(Pageable pageable) {
        User user = SecurityUtils.getUserFromPrincipal(userRepository);
        Integer totalComment = commentRepository.countAllCommentByUserId(user.getId()).orElse(0);
        CommentOverallResponse commentOverallResponse = new CommentOverallResponse();
        commentOverallResponse.setTotalComment(totalComment.intValue());
        Page<Comment> page = commentRepository.findAllByUserIdAndIsDeletedFalse(user.getId(), pageable);
        if (page.isEmpty()) throw new AppException(ErrorCode.NOT_FOUND);
        List<CommentResponse> data = page.getContent().stream().map(comment -> {
            CommentResponse commentResponse = commentMapper.toCommentResponseDTO(comment);
            commentResponse.setThumbnail(comment.getChapter().getBook().getThumbnail());
            CommentResponse.UserCommentDTO userCommentDTO = CommentResponse.UserCommentDTO.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .avatar(user.getAvatar())
                    .build();
            commentResponse.setUser(userCommentDTO);
            return commentResponse;
        }).toList();
        DataListResponse<CommentResponse> dataListResponse = new DataListResponse<>();
        dataListResponse.setData(data);
        dataListResponse.setCurrentPage(pageable.getPageNumber() + 1);
        dataListResponse.setTotalPages(page.getTotalPages());
        commentOverallResponse.setData(dataListResponse);
        return commentOverallResponse;
    }

    @Override
    public Integer countAllComment() {
        try {
            return commentRepository.countAllComment();
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}
