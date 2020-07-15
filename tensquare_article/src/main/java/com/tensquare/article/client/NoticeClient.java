package com.tensquare.article.client;

import com.tensquare.article.pojo.Notice;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "tensquare-notice")
public interface NoticeClient {

    @PostMapping("/notice")
    public Result save(@RequestBody Notice notice);

}
