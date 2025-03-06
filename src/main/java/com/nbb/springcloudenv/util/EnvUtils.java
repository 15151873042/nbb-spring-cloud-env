package com.nbb.springcloudenv.util;


import com.nbb.springcloudenv.config.EnvProperties;
import feign.RequestTemplate;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 胡鹏
 */
public class EnvUtils {

    private static final String HEADER_TAG = "tag";

    public static String getTag(HttpServletRequest request) {
        return request.getHeader(HEADER_TAG);
    }
    public static String getTag(ServiceInstance instance) {
        return instance.getMetadata().get(HEADER_TAG);
    }
    public static String getTag(Environment environment) {
        return environment.getProperty(EnvProperties.TAG_KEY);
    }

    public static void setTag(RequestTemplate requestTemplate, String tag) {
        requestTemplate.header(HEADER_TAG, tag);
    }
}
