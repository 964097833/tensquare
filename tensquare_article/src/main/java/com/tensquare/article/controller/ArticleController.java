package com.tensquare.article.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisTemplate redisTemplate;

    //测试全局异常接口
    @GetMapping("/exception")
    public Result exception() {
        int a = 1/0;
        return null;
    }

    @PostMapping("/search/{page}/{size}")
    public Result search(@RequestBody Map<String,Object> map,@PathVariable Integer page, @PathVariable Integer size) {
        Page page1 = articleService.search(map,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult((long)page1.getTotal(),page1.getRecords()));
    }

    //GET /article 文章全部列表
    @GetMapping
    public Result findAll() {
        List<Article> list = articleService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",list);
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable String id) {
        Article article = articleService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",article);
    }

    //新增标签数据接口
    @PostMapping
    public Result add(@RequestBody Article article) {
        articleService.add(article);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    //修改标签数据接口
    @PutMapping("/{id}")
    public Result update(@PathVariable String id,@RequestBody Article article) {
        article.setId(id);
        articleService.update(article);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    //删除文章数据接口
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        articleService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 订阅或取消订阅文章作者
     * @param map
     * @return
     */
    @PostMapping("/subscribe")
    private Result subscribe(@RequestBody Map<String,String> map) {
        Boolean flag =
                articleService.subscribe(map.get("userId").toString(),map.get("articleId").toString());

        if (flag) {
            return new Result(true,StatusCode.OK,"订阅成功");
        } else {
            return new Result(true,StatusCode.OK,"订阅取消");
        }
    }

    //文章点赞
    @PutMapping("/thumbup/{articleId}")
    public Result thumbup(@PathVariable String articleId) {
        //模拟用户id
        String userId = "1";
        String key = "thumbup_article_"+userId+"_"+articleId;

        //查询用户点赞信息，根据用户id和文章id
        Object flag = redisTemplate.opsForValue().get(key);

        //判断查询到的结果是否为空
        if (flag == null) {
            //如果为空，表示用户没有点过赞，可以点赞
            articleService.thumbup(articleId,userId);
            //点赞成功，保存点赞信息
            redisTemplate.opsForValue().set(key,1);

            return new Result(true,StatusCode.OK,"点赞成功");
        }

        //如果不为空，表示用户点过赞，不可以重复点赞
        return new Result(true,StatusCode.OK,"不能重复点赞");
    }
}
