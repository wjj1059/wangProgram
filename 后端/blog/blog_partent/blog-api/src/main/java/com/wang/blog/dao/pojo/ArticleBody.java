package com.wang.blog.dao.pojo;

import lombok.Data;

/**
 * @author 王家俊
 */
@Data
public class ArticleBody {
    private Long id;
    private String content;
    private String contentHtml;
    private Long articleId;
}
