package com.wang.blog.controller;

import com.wang.blog.common.aop.LogAnnotation;
import com.wang.blog.common.cache.Cache;
import com.wang.blog.service.ArticleService;
import com.wang.blog.vo.Result;
import com.wang.blog.vo.param.ArticleParam;
import com.wang.blog.vo.param.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 王家俊
 * Json数据交互
 */
@RestController
@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    /**
     * 首页 文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    @LogAnnotation(module = "文章",operator = "获取文章列表")
    @Cache(expire = 5*60*1000,name = "listArticle")
    public Result listArticle(@RequestBody PageParams pageParams){
        Result articles = articleService.listArticlePage(pageParams);
        return  articles;
    }

    /**
     * 首页最热文章
     * @return
     */
    @PostMapping("hot")
    @Cache(expire = 5*60*1000,name = "hot_article")
    public Result hotArticle(){
        int limit = 5;
        return articleService.hotArticle(limit);
    }
    /**
     * 最新文章
     */
    @PostMapping("new")
    @Cache(expire = 5*60*1000,name = "newArticles")
    public Result newArticles(){
        int limit = 5;
       return articleService.newArticles(limit);
    }
    /**
     * 文章归档
     */
    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    @PostMapping("/view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
      return articleService.findArticleById(articleId);
    }
    @PostMapping("/publish")
    public Result publish(@RequestBody ArticleParam articleParam){
      return  articleService.publish(articleParam);
    }
}
