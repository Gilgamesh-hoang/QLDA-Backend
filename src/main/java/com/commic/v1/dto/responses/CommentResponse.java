package com.commic.v1.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Integer id;
    private UserCommentDTO user;
    private String bookName;
    private String thumbnail;
    private String chapterName;
    private String content;
    private Date createdAt;
    private Integer state;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserCommentDTO {
        private String username;
        private String email;
        private String avatar;
    }
}
