package com.wang.blog.service;

import com.wang.blog.vo.CategoryVo;
import com.wang.blog.vo.Result;

import java.util.List;

/**
 * @author 王家俊
 */
public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result findCategoryDetailById(Long id);
}
