package com.wang.blog.vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @author 王家俊
 */
@Data
//@JsonSerialize(using = ToStringSerializer.class)
public class ArticleVo {
    //防止id精度损失，对id进行序列化转为String 类型
    //@JsonSerialize(using = ToStringSerializer.class)
    private String id;

    private String title;

    private String summary;  //简介

    private Integer commentCounts;

    private Integer ViewCounts;

    private Integer weight;   //置顶

    private String createDate;  //创建时间

    private String author;

    private ArticleBodyVo body;

    private List<TagVo> tags;

    private CategoryVo category;

}
