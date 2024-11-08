package com.commic.testComment;
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
import com.commic.v1.services.comment.CommentConst;
import com.commic.v1.services.comment.CommentGetType;
import com.commic.v1.services.comment.CommentServiceImp;
import com.commic.v1.util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class testCommentService {
    @InjectMocks
    private CommentServiceImp commentService;

    @Mock
    private IUserRepository userRepository;
    @Mock
    private IBookRepository bookRepository;
    @Mock
    private IChapterRepository chapterRepository;

    @Mock
    private ICommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;
    private Pageable pageable;
    private Comment comment;
    private CommentCreationRequestDTO requestDTO;
    @Mock
    private SecurityUtils securityUtils;
    @Mock
    private User mockUser;
    CommentResponse commentResponse;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setUsername("testUser");
        mockUser.setEmail("test@example.com");
        mockUser.setAvatar("avatar.png");

        requestDTO = new CommentCreationRequestDTO();
        requestDTO.setChapterId(1);

        comment = new Comment();
        comment.setCreatedAt(new Date());
        comment.setState(CommentConst.SHOW.getValue());
        comment.setUser(mockUser);

        commentResponse = new CommentResponse();
        commentResponse.setUser(CommentResponse.UserCommentDTO.builder()
                .username(mockUser.getUsername())
                .email(mockUser.getEmail())
                .avatar(mockUser.getAvatar())
                .build());
    }

    @Test
    public void testCreateComment_ChapterNotFound() {
        // Arrange
        CommentCreationRequestDTO requestDTO = new CommentCreationRequestDTO();
        requestDTO.setChapterId(99); // Changed to Long

        when(chapterRepository.existsChapterById(99)).thenReturn(false);

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> commentService.create(requestDTO));
        assertEquals(ErrorCode.CHAPTER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testGetCommentSuccess() {
        Integer chapterId = 41;
        Comment comment = new Comment();
        CommentDTO commentDTO = new CommentDTO();

        when(commentRepository.findByChapterId(chapterId)).thenReturn(Arrays.asList(comment));
        when(commentMapper.toCommentDTOs(comment)).thenReturn(commentDTO);

        List<CommentDTO> result = commentService.getComment(chapterId);

        assertEquals(1, result.size());
        assertEquals(commentDTO, result.get(0));
        verify(commentRepository, times(1)).findByChapterId(chapterId);
        verify(commentMapper, times(1)).toCommentDTOs(comment);
    }
    @Test
    void testGetCommentsSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Comment comment = new Comment();
        CommentResponse commentResponse = new CommentResponse();

        when(commentRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(comment)));
        when(commentMapper.toCommentResponseDTO(comment)).thenReturn(commentResponse);

        DataListResponse<CommentResponse> result = commentService.getComments(pageable);


        assertEquals(1, result.getCurrentPage());
        assertEquals(commentResponse, result.getData().get(0));

    }



    @Test
    void testGetCommentsByBookNotFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Integer bookId = 30;

        when(commentRepository.getCommentByBookId(bookId, pageable))
                .thenReturn(Page.empty());

        assertThrows(AppException.class, () -> {
            commentService.getComments(CommentGetType.BY_BOOK, bookId, pageable);
        });

        verify(commentRepository, times(1)).getCommentByBookId(bookId, pageable);
    }



}
