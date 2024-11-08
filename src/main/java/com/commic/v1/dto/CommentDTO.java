package com.commic.v1.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {
    private Integer id;
    private Integer userId;
    private Integer chapterId;
    private String content;
    private Date createdAt;
    private Integer state;
}
