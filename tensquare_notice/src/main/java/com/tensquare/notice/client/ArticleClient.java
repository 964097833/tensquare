package com.tensquare.notice.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "tensquare-article")
public interface ArticleClient {
    /**
     * 根据id查询文章
     * @param articleId
     * @return
     */
    @GetMapping("/article/{articleId}")
    public Result findById(@PathVariable String articleId);
}
