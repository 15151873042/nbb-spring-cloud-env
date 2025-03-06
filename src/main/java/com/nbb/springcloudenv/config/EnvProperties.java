package com.nbb.springcloudenv.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 胡鹏
 */
@ConfigurationProperties(prefix = "env")
@Data
public class EnvProperties {

    /** 环境变量中的tag值对应的key */
    public static final String TAG_KEY = "env.tag";

    private String tag;
}
