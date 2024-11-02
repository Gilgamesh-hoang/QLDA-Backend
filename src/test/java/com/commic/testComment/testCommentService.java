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
import com.commic.v1.services.comment.CommentGetType;
import com.commic.v1.services.comment.CommentServiceImp;
import com.commic.v1.util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


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

    @Mock
    private SecurityUtils securityUtils;
    @Mock
    private User mockUser;
    CommentResponse commentResponse;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Set up a mock user for testing
        mockUser = new User();
        mockUser.setUsername("123456");
        mockUser.setEmail("ducvui2003@gmail.com");
        mockUser.setAvatar("123456@gmail.com");

        // Mock the SecurityUtils method to always return the mock user
        mockStatic(SecurityUtils.class);
        when(SecurityUtils.getUserFromPrincipal(userRepository)).thenReturn(mockUser);
        MockitoAnnotations.openMocks(this);
        pageable = PageRequest.of(0, 10);
        comment = new Comment();
        // Set up comment fields here as needed
         commentResponse = new CommentResponse();
    }

    @Test
    public void testCreateComment_Success() {
        // Arrange
        CommentCreationRequestDTO requestDTO = new CommentCreationRequestDTO();
        requestDTO.setChapterId(1); // Changed to Long

        when(chapterRepository.existsChapterById(1)).thenReturn(true);

        Comment mockComment = new Comment();
        when(commentMapper.toComment(requestDTO)).thenReturn(mockComment);
        when(commentRepository.save(mockComment)).thenReturn(mockComment);

        CommentResponse expectedResponse = new CommentResponse();
        when(commentMapper.toCommentResponseDTO(mockComment)).thenReturn(expectedResponse);

        // Act
        CommentResponse response = commentService.create(requestDTO);

        // Assert
        assertEquals(expectedResponse, response);
        verify(commentRepository).save(mockComment);
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
        Integer chapterId = 1;
        Comment comment = new Comment();
        CommentDTO commentDTO = new CommentDTO();

        when(commentRepository.findByChapterId(chapterId)).thenReturn(Arrays.asList(comment));
        when(commentMapper.toCommentDTOs(comment)).thenReturn(commentDTO);

        List<CommentDTO> result = commentService.getComment(chapterId);

        assertEquals(3, result.size());
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

        assertEquals(120, result.getData().size());
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
