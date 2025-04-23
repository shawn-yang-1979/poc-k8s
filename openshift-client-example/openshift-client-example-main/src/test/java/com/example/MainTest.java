package com.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.server.mock.EnableKubernetesMockClient;
import io.fabric8.openshift.client.OpenShiftClient;

@SpringBootTest
@EnableKubernetesMockClient(crud = true)
class MainTest {

  @Autowired
  private OpenShiftClient client;

  @BeforeEach
  void arrange() {
    Pod newPod = new PodBuilder().withNewMetadata().withName("pod1").endMetadata().build();
    Pod pod = client.pods().inNamespace("default").resource(newPod).create();
    System.out.println(pod.getStatus());
  }

  @Test
  void simpleTestWithTargetVersion() {
    PodList pods = client.pods().inNamespace("default").list();
    pods.getItems().stream().forEach(s -> {
      System.out.println(s.getStatus());
    });
    assertNotNull(pods);
  }

}
