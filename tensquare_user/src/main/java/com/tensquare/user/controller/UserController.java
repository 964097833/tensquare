package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    //根据id查询用户
    @GetMapping("/{id}")
    public Result selectById(@PathVariable String id) {
        User user = userService.selectById(id);
        return new Result(true, StatusCode.OK,"查询成功",user);
    }

}
