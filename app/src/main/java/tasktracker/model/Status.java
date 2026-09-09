package tasktracker.model;

public enum Status {
    NEW,
    IN_PROGRESS,
    DONE;
    
    @Override
    public String toString() {
        return this.name();
    }
}