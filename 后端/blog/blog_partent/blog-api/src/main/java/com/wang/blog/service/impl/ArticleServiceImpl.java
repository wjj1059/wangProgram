package com.wang.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wang.blog.dao.dos.Archives;
import com.wang.blog.dao.mapper.ArticleBodyMapper;
import com.wang.blog.dao.mapper.ArticleMapper;
import com.wang.blog.dao.mapper.ArticleTagMapper;
import com.wang.blog.dao.mapper.TagMapper;
import com.wang.blog.dao.pojo.Article;
import com.wang.blog.dao.pojo.ArticleBody;
import com.wang.blog.dao.pojo.ArticleTag;
import com.wang.blog.dao.pojo.SysUser;
import com.wang.blog.service.*;
import com.wang.blog.utils.UserThreadLocal;
import com.wang.blog.vo.*;
import com.wang.blog.vo.param.ArticleParam;
import com.wang.blog.vo.param.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 王家俊
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public Result listArticlePage(PageParams pageParams) {
        Page<Article> page = new Page<Article>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records,true,true));
    }

    /*@Override
    public Result listArticlePage(PageParams pageParams) {
        //1. 这个是分页查询的类（代表着分离模式），要传入的是页面的页数和页面总数
        Page<Article> page = new Page<Article>(pageParams.getPage(),pageParams.getPageSize());

        //2. LambdaQueryWrapper是MybatisPlus提供的，需要就导入这个包就可以了
        List<Long> articleIdList = new ArrayList<>();
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if (pageParams.getCategoryId()!=null){
            //and
            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
        }if (pageParams.getTagId()!=null){
            //加入标签，条件查询
            //article表中 并没有tag字段 一篇文章有多个标签
            //article_tag article_id 1:n tag_id(一对多的关系)
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,pageParams.getTagId());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
            for (ArticleTag articleTag : articleTags) {
                articleIdList.add(articleTag.getId());
            }
            if (articleIdList.size()>0){
                //加入查询条件 and id in(1,2,3,4)
                queryWrapper.in(Article::getId,articleIdList);
            }
        }
        //3. 这里是根据字体排序
        //queryWrapper.orderByDesc(Article::getWeight);
        //4. 这里设置的是根据时间排序
        //queryWrapper.orderByDesc(Article::getCreateDate);
        //5. 这个方法    default Children orderByDesc(boolean condition, R column, R... columns) {是可变长参数的
        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);

        // 因为articleMapper继承了BaseMapper了的，所有设查询的参数和查询出来的排序方式
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);

        //这个就是我们查询出来的数据的数组了
        List<Article> records = articlePage.getRecords();

        //因为页面展示出来的数据不一定和数据库一样，所有我们要做一个转换
        //将在查出数据库的数组复制到articleVo中实现解耦合,vo和页面数据交互
        List<ArticleVo> articleVoList = copyList(records,true,true);

        return Result.success(articleVoList);
    }*/

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        //select id title from article order by view_count desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return  Result.success(copyList(articles,false,false));
    }

    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        //select id title from article order by create_date desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return  Result.success(copyList(articles,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 根据id查询文章信息
         * 根据bodyid和categoryid
         */
        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article,true,true,true,true);
        /**
         * 查看完文章了,新增阅读数
         * 查看完文章之后，本应该直接返回数据了，这时候做了一个更新操作
         * 更新加写锁，会阻塞其他读锁，性能就会降低
         * 更新增加此次接口的耗时
         * 如果更新出问题不影响查看文章操作
         * 线程池  可以把更新操作扔到线程池中区执行和主线程就不影响了
         */
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }

    /**
     * 1.发布文章 目的 构建Article对象
     * 2.作者id,当前用户登陆
     * 3.标签 要将标签加入到关联列表当中
     *4.body 内容存储 article bodyId
     */
    @Transactional
    @Override
    public Result publish(ArticleParam articleParam) {
        //此接口 需要加入到拦截中使其先登录，不然拿不到
        SysUser sysUser = UserThreadLocal.get();
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        //设置权重(不置顶)
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        //插入之后 会生成一个文章id
        this.articleMapper.insert(article);
        //tag
        List<TagVo> tags = articleParam.getTags();
        if (tags!=null){
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        Map<String, String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        //或者(需要序列化，序列化已加入)
            /*ArticleVo articleVo = new ArticleVo();
            articleVo.setId(article.getId());
            Result.success(articleVo);*/
        return Result.success(map);
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }

    //这个方法是主要点是BeanUtils，又Spring提供的，专门用来拷贝的，想Article和articlevo相同属性的拷贝过来返回
    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article,articleVo);
        //articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        LocalDateTime localDateTime = LocalDateTime.now();
        articleVo.setCreateDate(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        //并不是所有接口都需要标签，作者信息
        if (isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
           // articleVo.setAuthor("");
        }
        if (isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId = article.getCategoryId( );
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }
    @Autowired
    ArticleBodyMapper articleBodyMapper;
    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody  = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
    private CategoryVo findCategory(Long categoryId) {
        return categoryService.findCategoryById(categoryId);
    }
    private ArticleBodyVo findArticleBody(Long articleId) {
        LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getArticleId,articleId);
        ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }


}
