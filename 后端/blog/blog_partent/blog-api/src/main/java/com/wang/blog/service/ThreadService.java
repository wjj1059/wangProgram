package com.wang.blog.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wang.blog.dao.mapper.ArticleMapper;
import com.wang.blog.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author 王家俊
 */
@Component
public class ThreadService {
    //期望操作在线程池中更新
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article){
        int viewCounts = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts+1);
        //更新条件
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        //设置一个，为了在多线程的环境下线程安全
        updateWrapper.eq(Article::getViewCounts,viewCounts);
        //update article set view_count = 100 where view_count=99 and id=1
        articleMapper.update(articleUpdate,updateWrapper);
        try {
            Thread.sleep(3000);
            System.out.println("更新完成...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

