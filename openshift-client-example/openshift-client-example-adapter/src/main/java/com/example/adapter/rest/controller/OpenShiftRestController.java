/*
 * Copyright © 2024 IBM Corporation. IBM retains ownership, copyrights, any title of this source
 * code. IBM Confidential.
 */

package com.example.adapter.rest.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.openshift.client.OpenShiftClient;

@RestController
@RequestMapping
public class OpenShiftRestController {

  private final OpenShiftClient client;

  public OpenShiftRestController(OpenShiftClient client) {
    this.client = client;
  }

  @GetMapping(path = "/{namespace}/pods")
  public List<Pod> getPods(@PathVariable String namespace, @RequestParam String app) {
    return client.pods().inNamespace(namespace).withLabel("app", app).list().getItems();
  }
}
