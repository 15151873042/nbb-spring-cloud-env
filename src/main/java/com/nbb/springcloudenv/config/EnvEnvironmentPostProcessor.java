package com.nbb.springcloudenv.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.nbb.springcloudenv.util.EnvUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Set;

/**
 * @author 胡鹏
 */
public class EnvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final Set<String> CHANGE_TAG_KEYS = CollUtil.newHashSet(
            "spring.cloud.nacos.discovery.metadata.tag"
    );
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        String tag = EnvUtils.getTag(environment);
        if (StrUtil.isBlank(tag)) {
            return;
        }


        for (String targetTagKey : CHANGE_TAG_KEYS) {
            String targetTagValue = environment.getProperty(targetTagKey);
            if (StrUtil.isNotBlank(targetTagValue)) {
                continue;
            }

            environment.getSystemProperties().put(targetTagKey, tag);
        }
    }
}
