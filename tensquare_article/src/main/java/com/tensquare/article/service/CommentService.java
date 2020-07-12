package com.tensquare.article.service;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public Comment findById(String id) {
        return commentRepository.findById(id).get();
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public void save(Comment comment) {
        String id = idWorker.nextId()+"";
        comment.set_id(id);
        comment.setPublishdate(new Date());
        comment.setThumbup(0);
        commentRepository.save(comment);
    }

    public void update(Comment comment) {
        commentRepository.save(comment);
    }

    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> findByArticleId(String articleId) {
        return commentRepository.findByArticleid(articleId);
    }

    public void thumbup(String id) {
//        Comment comment = commentRepository.findById(id).get();
//        comment.setThumbup(comment.getThumbup()+1);
//        commentRepository.save(comment);

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query,update,"comment");
    }
}
