package com.wang.blog.dao.pojo;

import lombok.Data;

/**
 * @author 王家俊
 */
@Data
public class Comment {
    private Long id;

    private String content;

    private Long createDate;

    private Long articleId;

    private Long authorId;

    private Long parentId;

    private Long toUid;

    private Integer level;
}
