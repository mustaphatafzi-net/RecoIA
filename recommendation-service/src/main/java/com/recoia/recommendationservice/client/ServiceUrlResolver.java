package com.recoia.recommendationservice.client;

import com.recoia.recommendationservice.exception.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;

/** Demande a Eureka l'adresse d'un service a partir de son nom ("catalog-service" -> http://pc:8081). */
@Component
@RequiredArgsConstructor
public class ServiceUrlResolver {

    private final DiscoveryClient discoveryClient;

    public String url(String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (instances.isEmpty()) {
            throw new ServiceUnavailableException("Service introuvable dans Eureka : " + serviceName);
        }
        return instances.get(0).getUri().toString();
    }
}
