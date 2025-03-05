package com.nbb.springcloudenv.feign;

import cn.hutool.core.util.StrUtil;
import com.nbb.springcloudenv.context.EnvContextHolder;
import com.nbb.springcloudenv.util.EnvUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author 胡鹏
 */
public class EnvRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        String tag = EnvContextHolder.getTag();
        if (StrUtil.isNotBlank(tag)) {
            EnvUtils.setTag(template, tag);
        }
    }
}
