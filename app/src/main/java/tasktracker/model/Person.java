package tasktracker.model;

import java.util.UUID;
import java.util.Objects;

public class Person {
    public final UUID id;
    private String name;
    
    public Person(String name){
        this.id = UUID.randomUUID();
        this.name = name;
    }
    
    public Person(UUID id, String name){
        this.id = id;
        this.name = name;
    }
    
    public UUID getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    @Override 
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
    
    @Override
    public String toString(){
        return name;
    }
}
