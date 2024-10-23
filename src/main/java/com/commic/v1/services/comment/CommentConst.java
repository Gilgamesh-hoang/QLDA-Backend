package com.commic.v1.services.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum CommentConst {
    SHOW(1),
    HIDE(2);
    private Integer value;

    public static CommentConst fromValue(Integer value) {
        for (CommentConst commentConst : CommentConst.values()) {
            if (commentConst.getValue().equals(value)) {
                return commentConst;
            }
        }
        return null;
    }
}
