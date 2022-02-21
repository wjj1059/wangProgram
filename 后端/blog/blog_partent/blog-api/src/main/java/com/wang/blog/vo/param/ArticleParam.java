package com.wang.blog.vo.param;

import com.wang.blog.vo.CategoryVo;
import com.wang.blog.vo.TagVo;
import lombok.Data;

import java.util.List;

/**
 * @author 王家俊
 */
@Data
public class ArticleParam {
    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}
