package com.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.server.mock.EnableKubernetesMockClient;
import io.fabric8.openshift.client.OpenShiftClient;

@SpringBootTest
@EnableKubernetesMockClient(crud=true)
class MainTest {

  private static OpenShiftClient client;

  @Test
  void simpleTestWithTargetVersion() {
    // CREATE
    client.pods().inNamespace("ns1")
        .resource(new PodBuilder().withNewMetadata().withName("pod1").endMetadata().build())
        .create();
    PodList podList = client.pods().inNamespace("ns1").list();
    assertNotNull(podList);
  }

}
