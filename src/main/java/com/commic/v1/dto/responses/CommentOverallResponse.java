package com.commic.v1.dto.responses;

import com.commic.v1.dto.DataListResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentOverallResponse {
    private Integer totalComment;
    private DataListResponse<CommentResponse> data;
}
