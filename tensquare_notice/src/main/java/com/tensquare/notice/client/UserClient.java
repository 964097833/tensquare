package com.tensquare.notice.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "tensquare-user")
public interface UserClient {
    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    public Result findById(@PathVariable String id);
}
