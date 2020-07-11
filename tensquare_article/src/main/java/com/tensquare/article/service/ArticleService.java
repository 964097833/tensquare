package com.tensquare.article.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.dao.ArticleDao;
import com.tensquare.article.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private IdWorker idWorker;

    public List<Article> findAll() {
        return articleDao.selectList(null);
    }

    public Article findById(String id) {
        return articleDao.selectById(id);
    }

    public void add(Article article) {
        article.setId(idWorker.nextId()+"");
        articleDao.insert(article);
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
}
