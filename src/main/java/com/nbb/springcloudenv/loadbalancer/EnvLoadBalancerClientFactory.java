package com.nbb.springcloudenv.loadbalancer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClientsProperties;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.config.LoadBalancerAutoConfiguration;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;

/**
 * @author 胡鹏
 * {@link LoadBalancerAutoConfiguration#loadBalancerClientFactory(LoadBalancerClientsProperties)}
 */
public class EnvLoadBalancerClientFactory extends LoadBalancerClientFactory {

    public EnvLoadBalancerClientFactory(LoadBalancerClientsProperties properties) {
        super(properties);
    }

    /**
     * 获取指定服务ID的负载均衡实例
     *
     * @param serviceId 服务ID
     * @return 返回对应的ReactiveLoadBalancer<ServiceInstance>实例
     */
    @Override
    public ReactiveLoadBalancer<ServiceInstance> getInstance(String serviceId) {
        ReactiveLoadBalancer<ServiceInstance> originalLoadBalancer = super.getInstance(serviceId);

        ObjectProvider<ServiceInstanceListSupplier> lazyProvider = super.getLazyProvider(serviceId, ServiceInstanceListSupplier.class);
        // 参考 {@link com.alibaba.cloud.nacos.loadbalancer.NacosLoadBalancerClientConfiguration#nacosLoadBalancer(Environment, LoadBalancerClientFactory, NacosDiscoveryProperties)} 方法
        return new EnvLoadBalancer(lazyProvider, serviceId, originalLoadBalancer);

    }
}
