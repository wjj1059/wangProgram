package com.wang.blog.service;

import com.wang.blog.vo.Result;
import com.wang.blog.vo.param.ArticleParam;
import com.wang.blog.vo.param.PageParams;

/**
 * @author 王家俊
 */

public interface ArticleService {
    /**
     * 分页查询文章列表
     *
     */
   //Result  listArticlesPage(PageParams pageParams);
    Result listArticlePage(PageParams pageParams);

    Result hotArticle(int limit);

    Result newArticles(int limit);

    /**
     * 文章归档
     * @return
     */
    Result listArchives();

    /**
     * 查看文章详情
     * @param articleId
     * @return
     */
    Result findArticleById(Long articleId);

    /**
     * 文章发布服务
     * @param articleParam
     * @return
     */
    Result publish(ArticleParam articleParam);
}
