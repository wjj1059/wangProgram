package com.wang.blog.service;

import com.wang.blog.vo.Result;
import com.wang.blog.vo.TagVo;

import java.util.List;

/**
 * @author 王家俊
 */
public interface TagService {
    List<TagVo> findTagsByArticleId(Long articleId);

    List<TagVo> hot(int limit);

    /**
     * 查询所有文章标签
     * @return
     */
    Result findAll();

    Result findAllDetail();

    Result findAllDetailById(Long id);
}
