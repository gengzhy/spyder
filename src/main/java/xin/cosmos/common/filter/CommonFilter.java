package xin.cosmos.common.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;
import xin.cosmos.common.properties.CustomCorpProperties;
import xin.cosmos.common.util.ObjectsUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
@ServletComponentScan(value = {"xin.cosmos.controller"})
@WebFilter(urlPatterns = "/*", filterName = "commonFilter")
public class CommonFilter implements Filter {
    @Autowired
    private CustomCorpProperties customCorpProperties;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // 允许哪些Origin发起跨域请求
        String originHeader = httpServletRequest.getHeader("Allowed-Origin");
        CustomCorpProperties.AllowedOrigin allowedOrigin = customCorpProperties.getAllowedOrigin();
        if (allowedOrigin != null && allowedOrigin.isEnable() &&
                !ObjectsUtil.isNull(allowedOrigin.getAllowedUrls()) &&
                !Arrays.asList(allowedOrigin.getAllowedUrls()).contains(originHeader)) {
            httpServletResponse.setStatus(200);
            httpServletResponse.setCharacterEncoding("utf-8");
            httpServletResponse.setContentType("text/html; charset=utf-8");
            httpServletResponse.getWriter().println("<p style='color:red;text-align:center;'>当前请求不在允许访问跨域请求名单内，禁止访问！</p>");
            return;
        }
        httpServletResponse.setHeader("Access-Control-Allow-Origin", originHeader);
        // 允许请求的方法
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,PUT");
        // 多少秒内，不需要再发送预检验请求
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        // 表明它允许跨域请求包含xxx头
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "x-auth-token,Origin,Access-Token,X-Requested-With,Content-Type, Accept,multipart/form-data,Authorization");
        //是否允许浏览器携带用户身份信息（cookie）
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        if ("OPTIONS".equals(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(200);
            return;
        }
        chain.doFilter(request, response);
    }
}
