package com.tensquare.article.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleController {

    @Autowired
    private ArticleService articleService;

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

}
