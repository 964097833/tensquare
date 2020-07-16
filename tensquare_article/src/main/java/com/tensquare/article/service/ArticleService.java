package com.tensquare.article.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.client.NoticeClient;
import com.tensquare.article.dao.ArticleDao;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.pojo.Notice;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    private RabbitTemplate rabbitTemplate;

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

        rabbitTemplate.convertAndSend("article_subscribe",authorId,id);
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

        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate.getConnectionFactory());

        DirectExchange exchange = new DirectExchange("article_subscribe");
        rabbitAdmin.declareExchange(exchange);

        Queue queue = new Queue("article_subscribe_"+userId,true);

        Binding binding = BindingBuilder.bind(queue).to(exchange).with(authorId);

        String userKey = "article_subscribe_" + userId;
        String authorKey = "article_author_" + authorId;

        //查询该用户是否已经订阅作者
        Boolean flag = redisTemplate.boundSetOps(userKey).isMember(authorId);

        if (flag) {
            //如果flag为true，已经订阅，则取消订阅
            redisTemplate.boundSetOps(userKey).remove(articleId);
            redisTemplate.boundSetOps(authorKey).remove(userId);

            rabbitAdmin.removeBinding(binding);
            return false;
        } else {
            //如果flag为false，没有订阅，则进行订阅
            redisTemplate.boundSetOps(userKey).add(articleId);
            redisTemplate.boundSetOps(authorKey).add(userId);

            rabbitAdmin.declareQueue(queue);
            rabbitAdmin.declareBinding(binding);
            return true;
        }
    }

    public void thumbup(String articleId, String userId) {
        Article article = articleDao.selectById(articleId);
        article.setThumbup(article.getThumbup() + 1);
        articleDao.updateById(article);

        //点赞成功后，需要发送消息给文章作者（点对点消息）
        Notice notice = new Notice();
        // 接收消息用户的ID
        notice.setReceiverId(article.getUserid());
        // 进行操作用户的ID
        notice.setOperatorId(userId);
        // 操作类型（评论，点赞等）
        notice.setAction("publish");
        // 被操作的对象，例如文章，评论等
        notice.setTargetType("article");
        // 被操作对象的id，例如文章的id，评论的id'
        notice.setTargetId(articleId);
        // 通知类型
        notice.setType("user");

        //保存消息
        noticeClient.save(notice);

        //1 创建Rabbit管理器
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate.getConnectionFactory());

        //2 创建队列，每个用户都有自己的队列，通过用户id进行区分
        Queue queue = new Queue("article_thumbup_" + article.getUserid(), true);
        rabbitAdmin.declareQueue(queue);

        //3 发送消息
        rabbitTemplate.convertAndSend("article_thumbup_"+article.getUserid(),articleId);

    }
}
