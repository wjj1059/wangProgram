package com.wang.blog.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 王家俊
 */
@Data
public class CommentVo {
    private String id;

    private UserVo author;

    private String content;

    private List<CommentVo> childrens;

    private String createDate;

    private Integer level;

    private UserVo toUser;
}
