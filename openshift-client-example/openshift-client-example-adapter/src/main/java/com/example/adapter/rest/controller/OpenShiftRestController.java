/*
 * Copyright © 2024 IBM Corporation. IBM retains ownership, copyrights, any title of this source
 * code. IBM Confidential.
 */

package com.example.adapter.rest.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.fabric8.openshift.client.OpenShiftClient;

@RestController
@RequestMapping
public class OpenShiftRestController {

  private final OpenShiftClient client;

  public OpenShiftRestController(OpenShiftClient client) {
    this.client = client;
  }

  @GetMapping(path = "/projects")
  public List<String> getProjects() {
    return client.projects().list().getItems().stream().map(e -> e.getMetadata().getName())
        .collect(Collectors.toList());
  }

  @GetMapping(path = "/namespaces/{namespace}/pods")
  public List<String> getPods(@PathVariable String namespace) {
    return client.pods().inNamespace(namespace).list().getItems().stream()
        .map(e -> e.getMetadata().getName()).collect(Collectors.toList());
  }

  @GetMapping(path = "/namespaces/{namespace}/pods/{pod}/pod-ip")
  public String getPodIp(@PathVariable String namespace, @PathVariable String pod) {
    return client.pods().inNamespace(namespace).withName(pod).get().getStatus().getPodIP();
  }

  @GetMapping(path = "/namespaces/{namespace}/deployments")
  public List<String> getDeployments(@PathVariable String namespace) {
    return client.apps().deployments().inNamespace(namespace).list().getItems().stream()
        .map(e -> e.getMetadata().getName()).collect(Collectors.toList());
  }

}
