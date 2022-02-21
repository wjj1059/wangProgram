package com.wang.blog.controller;

import com.wang.blog.dao.pojo.Category;
import com.wang.blog.service.CategoryService;
import com.wang.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 王家俊
 */
@RestController
@RequestMapping("categorys")
public class CategoryController {
    /**
     * 所有分类
     * @return
     */
    @Autowired
    private CategoryService categoryService;
    @GetMapping
    public Result categories(){
        //查询所有
       return  categoryService.findAll();
    }
    @GetMapping("detail")
    public Result categoriesDetail(){
        return categoryService.findAllDetail();
    }
    @GetMapping("detail/{id}")
    public Result categoryDetailById(@PathVariable("id") Long categoryId){
      return categoryService.findCategoryDetailById(categoryId);
    }
}
