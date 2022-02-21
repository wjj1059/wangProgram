package com.wang.blog.vo.param;

import lombok.Data;

/**
 * @author 王家俊
 */
@Data
public class CommentParam {
    private Long articleId;

    private String content;

    private Long parent;

    private Long toUserId;
}
