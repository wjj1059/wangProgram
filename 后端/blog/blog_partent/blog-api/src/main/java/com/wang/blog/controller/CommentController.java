package com.wang.blog.controller;

import com.wang.blog.service.CommentsService;
import com.wang.blog.vo.Result;
import com.wang.blog.vo.param.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.lang.reflect.Method;

/**
 * @author 王家俊
 */
@RestController
@RequestMapping("comments")
public class CommentController {
    @Autowired
    CommentsService commentsService;
    //@RequestMapping(value = "/article",method = {RequestMethod.POST, RequestMethod.GET})
    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long id){
     return commentsService.commentsByArticleId(id);
    }
    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
       return commentsService.comment(commentParam);
    }
}
