package com.commic.v1.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentChangeDTO {
    private Integer commentId;
    private Integer state;
}
