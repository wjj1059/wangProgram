package com.wang.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wang.blog.dao.mapper.CommentMapper;
import com.wang.blog.dao.pojo.Comment;
import com.wang.blog.dao.pojo.SysUser;
import com.wang.blog.service.CommentsService;
import com.wang.blog.service.SysUserService;
import com.wang.blog.utils.UserThreadLocal;
import com.wang.blog.vo.CommentVo;
import com.wang.blog.vo.Result;
import com.wang.blog.vo.UserVo;
import com.wang.blog.vo.param.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 王家俊
 */
@Service
public class CommentsServiceImpl implements CommentsService {
    /**
     * 1.根据文章id 查询 评论列表 从comment表中查询
     * 2.根据作者id 查询作者信息
     * 3.判断如果 level = 1，要去查询它有没有子评论
     * 4.如果有，根据评论id 进行查询(parent_id)
     * @param id
     * @return
     */
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    SysUserService sysUserService;
    @Override
    public Result commentsByArticleId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,id);
        queryWrapper.eq(Comment::getLevel,1);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVos = copyList(comments);
        return Result.success(commentVos);
    }

    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);
        return Result.success(null);
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVos = new ArrayList<>();;
        for (Comment comment : comments) {
            commentVos.add(copy(comment));
        }
        return commentVos;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        commentVo.setId(String.valueOf(comment.getId()));
        //作者信息
        Long authorId = comment.getAuthorId();
        UserVo userVo = this.sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        //子评论
        Integer level = comment.getLevel();
        if (level==1){
            Long id = comment.getId();
            List<CommentVo> commentVoList=findCommentsParentsId(id);
            commentVo.setChildrens(commentVoList);
        }
        //to User 给谁评论
        if (level>1){
            Long toUid = comment.getToUid();
            UserVo userVoById = this.sysUserService.findUserVoById(toUid);
            commentVo.setToUser(userVoById);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsParentsId(Long id) {
    LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Comment::getParentId,id);
    queryWrapper.eq(Comment::getLevel,2);
    return copyList(commentMapper.selectList(queryWrapper));
    }


}
