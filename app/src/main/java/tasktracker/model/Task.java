package tasktracker.model;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Objects;

public class Task {
    private final UUID id;
    private String title;
    private String description;
    private Status status;
    private Person assignee;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Конструктор для новой задачи
    public Task (String title, String description, Person assignee){
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.assignee = assignee;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Конструктор для загрузки из файла (с указанием всех полей)
    public Task (UUID id, String title, String description, Status status,
                Person assignee, LocalDateTime createdAt, LocalDateTime updatedAt){
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.assignee = assignee;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Геттеры
    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; }
    public Person getAssignee() { return assignee; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    // Сеттеры с автоматическим обновлением updatedAt
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
    
    public void setAssignee(Person assignee) {
        this.assignee = assignee;
        this.updatedAt = LocalDateTime.now();
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
        return String.format("Task{id=%s, title='%s', status=%s, assignee=%s}", 
            id.toString().substring(0, 8), title, status, assignee);
    }
}
