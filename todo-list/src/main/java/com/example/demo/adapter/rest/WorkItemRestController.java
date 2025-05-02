package com.example.demo.adapter.rest;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.domain.workitem.WorkItem;
import com.example.demo.domain.workitem.WorkItemRepository;

@RestController
@RequestMapping(path = "/work-items")
public class WorkItemRestController {

  private final WorkItemRepository workItemRepository;

  public WorkItemRestController(WorkItemRepository workItemRepository) {
    this.workItemRepository = workItemRepository;
  }

  @GetMapping
  public List<WorkItem> getAll() {
    return workItemRepository.findAll();
  }

  @GetMapping(path = "/{id}")
  public WorkItem get(@PathVariable("id") Long id) {
    return workItemRepository.findById(id).orElseThrow(IllegalStateException::new);
  }

  @PostMapping
  public long post(@RequestBody Post param) {
    WorkItem item = new WorkItem();
    item.setDescription(param.getDescription());
    return workItemRepository.save(item).getId();
  }

  @PutMapping(path = "/{id}")
  public void put(Long id, @RequestBody Put param) {
    WorkItem workItem = workItemRepository.findById(id).orElseThrow(IllegalStateException::new);
    workItem.throwIfConflict(param.getVersion());
    workItem.setDescription(param.getDescription());
    workItemRepository.save(workItem);
  }

  @DeleteMapping(path = "/{id}")
  public void delete(Long id, @RequestBody Delete param) {
    WorkItem workItem = workItemRepository.findById(id).orElseThrow(IllegalStateException::new);
    workItem.throwIfConflict(param.getVersion());
    workItemRepository.delete(workItem);
  }

  public static class Post {
    private String description;

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }
  }

  public static class Put {
    private String description;
    private long version;

    public long getVersion() {
      return version;
    }

    public void setVersion(long version) {
      this.version = version;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }
  }

  public static class Delete {
    private long version;

    public long getVersion() {
      return version;
    }

    public void setVersion(long version) {
      this.version = version;
    }

  }
}
