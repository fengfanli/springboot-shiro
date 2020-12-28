package com.feng.service;

import com.feng.bean.SysUser;
import com.feng.vo.LoginReqVO;
import com.feng.vo.LoginRespVO;

public interface UserService {

    LoginRespVO login(LoginReqVO loginReqVO);

    SysUser detail(String id);


}
