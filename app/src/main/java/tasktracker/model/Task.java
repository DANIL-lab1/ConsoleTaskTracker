package tasktracker.model;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {
    private final UUID id;
    private String title;
    private String description;
    private Status status;
    private Person assignee;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Priority priority;
    private LocalDateTime deadline;
    
    @JsonCreator
    public Task(
        @JsonProperty("id") UUID id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("status") Status status,
        @JsonProperty("priority") Priority priority,
        @JsonProperty("assignee") Person assignee,
        @JsonProperty("deadline") LocalDateTime deadline,
        @JsonProperty("createdAt") LocalDateTime createdAt,
        @JsonProperty("updatedAt") LocalDateTime updatedAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assignee = assignee;
        this.deadline = deadline;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Конструктор для новой задачи
    public Task (String title, String description, Person assignee){
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.priority = Priority.MEDIUM;
        this.assignee = assignee;
        this.deadline = null;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Gets
    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; }
    public Priority getPriority() { return priority; }
    public Person getAssignee() { return assignee; }
    public LocalDateTime getDeadline() { return deadline; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    // Sets
    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setStatus(Status status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setPriority(Priority priority) {
        this.priority = priority;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setAssignee(Person assignee) {
        this.assignee = assignee;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
        this.updatedAt = LocalDateTime.now();
    }
    
    @JsonIgnore
    public boolean isOverdue() {
        return deadline != null && deadline.isBefore(LocalDateTime.now());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Task{id=%s, title='%s', status=%s, priority=%s, assignee=%s}",
            id.toString().substring(0, 8), title, status, priority, assignee);
    }
}
