package com.commic.v1.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChapterContentResponse {
    private Integer id;
    private Integer chapterId;
    private String linkImage;
}
