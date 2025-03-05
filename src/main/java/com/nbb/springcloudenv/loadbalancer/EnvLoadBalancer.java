package com.nbb.springcloudenv.loadbalancer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.balancer.NacosBalancer;
import com.nbb.springcloudenv.context.EnvContextHolder;
import com.nbb.springcloudenv.util.EnvUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 胡鹏
 */
@Slf4j
public class EnvLoadBalancer implements ReactorServiceInstanceLoadBalancer  {

    /**
     * 用于获取 serviceId 对应的服务实例的列表
     */
    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    /**
     * 需要获取的服务实例名
     */
    private String serviceId;
    /**
     * 被代理的 ReactiveLoadBalancer 对象
     */
    private ReactiveLoadBalancer<ServiceInstance> originLoadBalancer;


    public EnvLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                           String serviceId,
                           ReactiveLoadBalancer<ServiceInstance> originLoadBalancer) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.serviceId = serviceId;
        this.originLoadBalancer = originLoadBalancer;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        String tag = EnvContextHolder.getTag();
        if (StrUtil.isBlank(tag)) {
            return Mono.from(originLoadBalancer.choose());
        }

        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(instanceList -> this.getInstanceResponse(instanceList, tag));


    }


    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, String tag) {
        // 如果服务实例为空，则直接返回
        if (CollUtil.isEmpty(instances)) {
            log.warn("serviceId为【{}】的服务实例列表为空]", serviceId);
            return new EmptyResponse();
        }

        // 筛选满足条件的实例列表
        List<ServiceInstance> chooseInstances = instances.stream()
                .filter(instance -> tag.equals(EnvUtils.getTag(instance)))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(chooseInstances)) {
            log.warn("serviceId为【{}】 没有满足 tag为【{}】 的服务实例列表，直接使用所有服务实例列表]", serviceId, tag);
            chooseInstances = instances;
        }


        // 基于nacos 随机 + 权重 获取
        return new DefaultResponse(NacosBalancer.getHostByRandomWeight3(chooseInstances));
    }
}
