package com.wang.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wang.blog.dao.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 王家俊
 */
@Mapper
@Repository
public interface CommentMapper extends BaseMapper<Comment> {
}
