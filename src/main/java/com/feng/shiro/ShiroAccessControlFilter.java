package com.feng.shiro;

import com.alibaba.fastjson.JSON;
import com.feng.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName: CustomAccessControlerFilter
 * @Description： 自定义的 token 过滤器。
 * @createTime:
 * @Author: 冯凡利
 * @UpdateUser: 冯凡利
 * @Version: 0.0.1
 */

/**
 * 这里的异常，全局异常无法处理，比较高级没有到达 方法，所以需要自己处理  try-catch
 */
@Slf4j
public class ShiroAccessControlFilter extends AccessControlFilter {
    /**
     * 是否 允许 访问下一层
     * true： 允许，交下一个Filter 处理
     * false： 交给自己处理，往下执行 onAccessDenied 方法
     * @param servletRequest
     * @param servletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    /**
     * 表示访问拒绝时是否自己处理，
     * 如果返回 true 表示自己不处理且 继续拦截器执行，往下执行
     * 返回 false 表示自己已经处理了（比如重定向到另一个界面）处理完毕。
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        log.info(request.getMethod());
        log.info(request.getRequestURL().toString());
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //判断客户端是否携带 FENG_TEST
        try {
            String accessToken=request.getHeader("FENG_TEST");
            if(StringUtils.isEmpty(accessToken)){  // 判断 FENG_TEST 是否为空
                System.out.println("FENG_TEST不存在，报错");;
            }
            UsernamePasswordToken token=new UsernamePasswordToken(username, password);
            getSubject(servletRequest,servletResponse).login(token);
            // 登录之后， 进入CustomRealm类 进入认证与授权
        } catch (Exception e) {
            log.info("ShiroAccessControlFilter.onAccessDenied() 出错了");
            customRsponse(500,e.getLocalizedMessage(),servletResponse);
            return false; // 直接返回客户端
        }
        return true;
    }

    /**
     * 异常处理
     * 因为这里的位置是高于业务层的，所以这里的异常只能通过流的形式输出到前端。
     * @param code
     * @param msg
     * @param response
     */
    private void customRsponse(int code, String msg, ServletResponse response) {
        // 自定义异常的类，用户返回给客户端相应的JSON格式的信息
        try {
            DataResult result = DataResult.getResult(code, msg);
            response.setContentType("application/json; charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            String userJson = JSON.toJSONString(result);
            // 写入到 流中，返回到客户端
            OutputStream out = response.getOutputStream();
            out.write(userJson.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            log.error("eror={}", e.getLocalizedMessage());
        }
    }
}
