package com.tensquare.article.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.client.NoticeClient;
import com.tensquare.article.dao.ArticleDao;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.pojo.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private NoticeClient noticeClient;

    public List<Article> findAll() {
        return articleDao.selectList(null);
    }

    public Article findById(String id) {
        return articleDao.selectById(id);
    }

    public void add(Article article) {
        String id = idWorker.nextId() + "";
        article.setId(id);
        //初始化数据
        article.setVisits(0);
        article.setThumbup(0);
        article.setComment(0);

        articleDao.insert(article);

        //TODO 使用jwt获取当前用户的userId，也就是文章作者的id
        String authorId = "1";
        article.setUserid(authorId);

        //获取需要通知的读者
        String authorKey = "article_author_" + authorId;
        Set<String> set = redisTemplate.boundSetOps(authorKey).members();

        for (String uid : set) {
            //消息通知
            Notice notice = new Notice();
            notice.setReceiverId(uid);
            notice.setOperatorId(authorId);
            notice.setAction("publish");
            notice.setTargetType("article");
            notice.setTargetId(id);
            notice.setCreatetime(new Date());
            notice.setType("sys");
            notice.setState("0");
            noticeClient.save(notice);
        }
    }

    public void update(Article article) {
        articleDao.updateById(article);
    }

    public void delete(String id) {
        articleDao.deleteById(id);
    }

    public Page search(Map<String,Object> map, Integer page, Integer size) {
        EntityWrapper<Article> wrapper = new EntityWrapper<>();
        Set<String> fieldSet = map.keySet();
        for (String field : fieldSet) {
            wrapper.eq(null != map.get(field), field, map.get(field));
        }

        Page page1 = new Page(page, size);
        List<Article> list = articleDao.selectPage(page1, wrapper);
        page1.setRecords(list);
        return page1;
    }

    public Boolean subscribe(String userId, String articleId) {
        //根据文章id查询文章作者id
        String authorId = articleDao.selectById(articleId).getUserid();

        String userKey = "article_subscribe_" + userId;
        String authorKey = "article_author_" + authorId;

        //查询该用户是否已经订阅作者
        Boolean flag = redisTemplate.boundSetOps(userKey).isMember(authorId);

        if (flag) {
            //如果flag为true，已经订阅，则取消订阅
            redisTemplate.boundSetOps(userKey).remove(articleId);
            redisTemplate.boundSetOps(authorKey).remove(userId);
            return false;
        } else {
            //如果flag为false，没有订阅，则进行订阅
            redisTemplate.boundSetOps(userKey).add(articleId);
            redisTemplate.boundSetOps(authorKey).add(userId);
            return true;
        }
    }

    public void thumbup(String articleId,String userId) {
        //文章点赞
        Article article = articleDao.selectById(articleId);
        article.setThumbup(article.getThumbup()+1);
        articleDao.updateById(article);

        //消息通知
        Notice notice = new Notice();
        notice.setReceiverId(article.getUserid());
        notice.setOperatorId(userId);
        notice.setAction("thumbup");
        notice.setTargetType("article");
        notice.setTargetId(articleId);
        notice.setCreatetime(new Date());
        notice.setType("user");
        notice.setState("0");

        noticeClient.save(notice);
    }
}
