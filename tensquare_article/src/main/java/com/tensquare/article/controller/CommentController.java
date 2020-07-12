package com.tensquare.article.controller;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.service.CommentService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private RedisTemplate redisTemplate;

    //根据id查询评论
    @GetMapping("/{id}")
    public Result findById(@PathVariable String id) {
        Comment comment = commentService.findById(id);
        return new Result(true, StatusCode.OK,"查询成功",comment);
    }

    //查询所有
    @GetMapping
    public Result findAll() {
        List<Comment> list = commentService.findAll();
        return new Result(true,StatusCode.OK,"查询成功",list);
    }

    //新增
    @PostMapping
    public Result save(@RequestBody Comment comment) {
        commentService.save(comment);
        return new Result(true,StatusCode.OK,"新增成功");
    }

    //修改
    @PutMapping("/{id}")
    public Result update(@PathVariable String id, @RequestBody Comment comment) {
        comment.set_id(id);
        commentService.update(comment);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    //删除
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable String id) {
        commentService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    //根据文章id查询评论列表
    @GetMapping("/article/{articleId}")
    public Result findByArticleId(@PathVariable String articleId) {
        List<Comment> list = commentService.findByArticleId(articleId);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }

    //评论点赞
    @PutMapping("/thumbup/{id}")
    public Result thumbup(@PathVariable String id) {
        //模拟获取到了用户id
        String userid = "123";
        //redis中查询用户是否已经点赞
        Object result = redisTemplate.opsForValue().get("thumbup_" + userid + "_" + id);
        //如果点赞不能重复点
        if (result != null) {
            return new Result(false,StatusCode.ERROR,"不能重复点赞");
        }
        //如果没有点赞，可以进行点赞操作
        commentService.thumbup(id);
        //保存点赞记录
        redisTemplate.opsForValue().set("thumbup_"+userid+"_"+id,1);
        return new Result(true,StatusCode.OK,"点赞成功");
    }
}
