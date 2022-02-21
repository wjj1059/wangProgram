package com.wang.blog.dao.pojo;

import lombok.Data;

/**
 * @author 王家俊
 */
@Data
public class ArticleTag {

    private Long id;

    private Long articleId;

    private Long tagId;
}
