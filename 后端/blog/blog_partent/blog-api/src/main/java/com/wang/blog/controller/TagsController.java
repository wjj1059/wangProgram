package com.wang.blog.controller;

import com.wang.blog.service.TagService;
import com.wang.blog.vo.Result;
import com.wang.blog.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 王家俊
 */
@RestController
@RequestMapping("tags")
public class TagsController {

    @Autowired
    private TagService tagService;
    @GetMapping("/hot")
    public Result listHotTags(){
        int limit = 5;
        List<TagVo> TagHotList = tagService.hot(limit);
        return  Result.success(TagHotList);
    }
    @GetMapping()
    public Result findaAll(){
        return  tagService.findAll();
    }
    @GetMapping("detail")
    public Result categoriesDetail(){
        return tagService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result findAllDetail(@PathVariable Long id){
        return tagService.findAllDetailById(id);
    }
}
