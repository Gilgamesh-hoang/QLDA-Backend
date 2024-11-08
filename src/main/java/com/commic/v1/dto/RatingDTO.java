package com.commic.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {
    private Integer id;
    private Integer chapterId;
    private Integer userId;
    private Float star;
    private Date createdAt;
    private Boolean isDeleted;
}
