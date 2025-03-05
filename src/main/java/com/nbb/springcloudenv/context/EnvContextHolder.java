package com.nbb.springcloudenv.context;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 胡鹏
 * 使用 {@link List}存储，是为了多次设置tag值
 */
public class EnvContextHolder {

    private static final ThreadLocal<List<String>> TAG_CONTEXT = TransmittableThreadLocal.withInitial(ArrayList::new);

    public static void setTag(String tag) {
        TAG_CONTEXT.get().add(tag);
    }

    public static String getTag() {
        return CollUtil.getLast(TAG_CONTEXT.get());
    }

    public static void removeTag() {
        List<String> tags = TAG_CONTEXT.get();

        if (CollUtil.isEmpty(tags)) {
            return;
        }
        tags.remove(tags.size() - 1);
    }


}
