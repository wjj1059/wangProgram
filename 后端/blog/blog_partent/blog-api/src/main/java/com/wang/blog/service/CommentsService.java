package com.wang.blog.service;

import com.wang.blog.vo.Result;
import com.wang.blog.vo.param.CommentParam;
import org.springframework.stereotype.Repository;

/**
 * @author 王家俊
 */
@Repository
public interface CommentsService {
    /**
     * 根据文章ID查询所有列表
     * @param id
     * @return
     */
    Result commentsByArticleId(Long id);

    Result comment(CommentParam commentParam);
}
