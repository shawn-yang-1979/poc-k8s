package com.example;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

@Configuration
public class MainConfiguration {

  @Bean
  RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.build();
  }

  @Bean
  OpenShiftClient openShiftClient(OpenShiftClientProperties properties) {
    if (properties.getMasterUrl() != null && properties.getOauthToken() != null) {
      Config config = new ConfigBuilder()//
          .withMasterUrl(properties.getMasterUrl())//
          .withOauthToken(properties.getOauthToken())//
          .build();
      return new KubernetesClientBuilder().withConfig(config).build().adapt(OpenShiftClient.class);
    } else {
      return new KubernetesClientBuilder().build().adapt(OpenShiftClient.class);
    }
  }
}
