package com.nbb.springcloudenv.web;

import cn.hutool.core.lang.Chain;
import cn.hutool.core.util.StrUtil;
import com.nbb.springcloudenv.context.EnvContextHolder;
import com.nbb.springcloudenv.util.EnvUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author 胡鹏
 */
public class EnvWebFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tag = EnvUtils.getTag(request);
        if (StrUtil.isBlank(tag)) {
            filterChain.doFilter(request, response);
            return;
        }

        EnvContextHolder.setTag(tag);
        try {
            filterChain.doFilter(request, response);
        } finally {
            EnvContextHolder.removeTag();
        }
    }
}
