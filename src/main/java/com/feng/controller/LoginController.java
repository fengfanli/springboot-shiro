package com.feng.controller;

import com.feng.bean.SysUser;
import com.feng.service.UserService;
import com.feng.vo.LoginReqVO;
import com.feng.vo.LoginRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "用户模块",description = "用户模块相关接口")
public class LoginController {


    @Autowired
    private UserService userService;


    @GetMapping("/page")
    public String index() {
        return "login";
    }


    /**
     * 前端用表单发请求如果使用 form-data、x-www-form-urlencoded 获取 ，则不可用 @RequestBody接受（因为他接受的为json）
     * @param loginReqVO
     * @return
     */
    @ApiOperation(value = "用户登录接口")
    @PostMapping(value = "/login")
    @ResponseBody
    public Map<String, Object> loginUser(@RequestBody LoginReqVO loginReqVO) {
        LoginRespVO info = userService.login(loginReqVO);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", info);
        return result;
    }

    @ApiOperation(value = "获取用户详情接口")
    @GetMapping("/getuser/{id}")
    @RequiresPermissions("sys:user:detail")
    public Map<String, Object> getUserAllInfo(@PathVariable("id") String id){
        Map<String, Object> result = new HashMap<>();
        SysUser detail = userService.detail(id);
        result.put("code", 0);
        result.put("data", detail);
        return result;
    }

    @GetMapping("/test")
    public Map<String, Object> test(){
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", "sucess");
        return result;
    }

}