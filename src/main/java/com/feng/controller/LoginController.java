package com.feng.controller;

import com.feng.bean.SysUser;
import com.feng.service.UserService;
import com.feng.utils.DataResult;
import com.feng.vo.LoginReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {


    @Autowired
    private UserService userService;


    @GetMapping("/page")
    public String index() {
        return "login";
    }


    @RequestMapping("/loginUser")
    public DataResult loginUser(HttpServletRequest request, @RequestBody LoginReqVO loginReqVO) {
        HttpSession session = request.getSession();
        SysUser userInfo = userService.getUserInfoByUsername(loginReqVO.getUsername());
        DataResult dataResult = null;
        if (userInfo.getPassword().equals(loginReqVO.getPassword())){
            dataResult = DataResult.getResult(0, "success");
            dataResult.setData(userInfo);
        }else{
            dataResult = new DataResult(1, "false");
        }
        return dataResult;
    }

    @GetMapping("/getUser")
    public DataResult getUserAllInfo(){

        return null;
    }











}