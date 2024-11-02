package com.commic.testComment;
import com.commic.v1.api.user.CommentController;
import com.commic.v1.dto.DataListResponse;
import com.commic.v1.dto.requests.CommentCreationRequestDTO;
import com.commic.v1.dto.responses.APIResponse;
import com.commic.v1.dto.responses.CommentOverallResponse;
import com.commic.v1.dto.responses.CommentResponse;
import com.commic.v1.services.comment.CommentGetType;
import com.commic.v1.services.comment.ICommentServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
public class testCommentController {
    private MockMvc mockMvc;

    @Mock
    private ICommentServices commentServices;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    public void testCreateComment() throws Exception {
        CommentCreationRequestDTO requestDTO = new CommentCreationRequestDTO();
        requestDTO.setChapterId(1);
        requestDTO.setContent("Test comment");

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(1);
        commentResponse.setContent("Test comment");

        APIResponse<CommentResponse> apiResponse = new APIResponse<>();
        apiResponse.setResult(commentResponse);

        when(commentServices.create(any(CommentCreationRequestDTO.class))).thenReturn(commentResponse);

        mockMvc.perform(post("/api/v1/comment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"chapterId\": 1, \"content\": \"Test comment\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"result\":{\"id\":1,\"content\":\"Test comment\"}}"));
    }

    @Test
    public void testGetComments() throws Exception {
        DataListResponse<CommentResponse> dataListResponse = new DataListResponse<>();
        dataListResponse.setData(Collections.emptyList());
        dataListResponse.setCurrentPage(1);
        dataListResponse.setTotalPages(0);

        APIResponse<DataListResponse<CommentResponse>> apiResponse = new APIResponse<>();
        apiResponse.setResult(dataListResponse);

        when(commentServices.getComments(eq(CommentGetType.BY_CHAPTER), eq(1), any(Pageable.class)))
                .thenReturn(dataListResponse);

        mockMvc.perform(get("/api/v1/comment/chapter")
                        .param("type", "BY_CHAPTER") // Use the correct enum value
                        .param("id", "1")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"result\":{\"data\":[],\"currentPage\":1,\"totalPages\":0}}"));
    }

    @Test
    public void testGetCommentOverall() throws Exception {
        CommentOverallResponse commentOverallResponse = new CommentOverallResponse();
        commentOverallResponse.setTotalComment(5);

        APIResponse<CommentOverallResponse> apiResponse = new APIResponse<>();
        apiResponse.setResult(commentOverallResponse);

        when(commentServices.getCommentOverall(any(Pageable.class))).thenReturn(commentOverallResponse);

        mockMvc.perform(get("/api/v1/comment/user")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"result\":{\"totalComment\":5}}"));
    }

}
