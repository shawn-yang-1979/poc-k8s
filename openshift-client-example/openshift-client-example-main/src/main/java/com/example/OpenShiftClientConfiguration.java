package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

@Configuration
public class OpenShiftClientConfiguration {

  @Bean
  OpenShiftClient openShiftClient(OpenShiftClientProperties properties) {
    if (properties.getMasterUrl() != null && properties.getOauthToken() != null) {
      Config config =
          new ConfigBuilder().withOauthToken("sha256~8rtdYOKPfGGj5bCI1JT5ZVa3TEp-1jwZGPuUE6A4skY")
              .withMasterUrl("https://api.rm2.thpm.p1.openshiftapps.com:6443").build();
      return new KubernetesClientBuilder().withConfig(config).build().adapt(OpenShiftClient.class);
    } else {
      return new KubernetesClientBuilder().build().adapt(OpenShiftClient.class);
    }
  }
}
