package com.nepxion.discovery.plugin.framework.decorator;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.concurrent.atomic.AtomicReference;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperRegistration;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperServiceRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import com.nepxion.discovery.plugin.framework.context.PluginContextAware;
import com.nepxion.discovery.plugin.framework.listener.register.RegisterListenerExecutor;

public class ZookeeperServiceRegistryDecorator extends ZookeeperServiceRegistry {
    private ZookeeperServiceRegistry serviceRegistry;
    private ConfigurableApplicationContext applicationContext;
    private ConfigurableEnvironment environment;

    public ZookeeperServiceRegistryDecorator(ZookeeperServiceRegistry serviceRegistry, ConfigurableApplicationContext applicationContext) {
        super(null);

        this.serviceRegistry = serviceRegistry;
        this.applicationContext = applicationContext;
        this.environment = applicationContext.getEnvironment();
    }

    @Override
    public void register(ZookeeperRegistration registration) {
        Boolean registerControlEnabled = PluginContextAware.isRegisterControlEnabled(environment);
        if (registerControlEnabled) {
            RegisterListenerExecutor registerListenerExecutor = applicationContext.getBean(RegisterListenerExecutor.class);
            registerListenerExecutor.onRegister(registration);
        }

        serviceRegistry.register(registration);
    }

    @Override
    public void deregister(ZookeeperRegistration registration) {
        Boolean registerControlEnabled = PluginContextAware.isRegisterControlEnabled(environment);
        if (registerControlEnabled) {
            RegisterListenerExecutor registerListenerExecutor = applicationContext.getBean(RegisterListenerExecutor.class);
            registerListenerExecutor.onDeregister(registration);
        }

        serviceRegistry.deregister(registration);
    }

    @Override
    public void setStatus(ZookeeperRegistration registration, String status) {
        Boolean registerControlEnabled = PluginContextAware.isRegisterControlEnabled(environment);
        if (registerControlEnabled) {
            RegisterListenerExecutor registerListenerExecutor = applicationContext.getBean(RegisterListenerExecutor.class);
            registerListenerExecutor.onSetStatus(registration, status);
        }

        serviceRegistry.setStatus(registration, status);
    }

    @Override
    public Object getStatus(ZookeeperRegistration registration) {
        return serviceRegistry.getStatus(registration);
    }

    @Override
    public void close() {
        Boolean registerControlEnabled = PluginContextAware.isRegisterControlEnabled(environment);
        if (registerControlEnabled) {
            RegisterListenerExecutor registerListenerExecutor = applicationContext.getBean(RegisterListenerExecutor.class);
            registerListenerExecutor.onClose();
        }

        serviceRegistry.close();
    }

    @Override
    public void afterSingletonsInstantiated() {
        serviceRegistry.afterSingletonsInstantiated();
    }

    @Deprecated
    public CuratorFramework getCurator() {
        return serviceRegistry.getCurator();
    }

    @Deprecated
    public AtomicReference<ServiceDiscovery<ZookeeperInstance>> getServiceDiscoveryRef() {
        return serviceRegistry.getServiceDiscoveryRef();
    }

    public ConfigurableEnvironment getEnvironment() {
        return environment;
    }
}