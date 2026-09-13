package tasktracker.ui.commands;

import tasktracker.service.TaskService;

public class ClearCommand implements ICommand {
    
    @Override
    public void execute(String[] args, TaskService service) {
        service.deleteAllTasks();
        System.out.println("SUCCESS: All tasks cleared!");
    }
    
    @Override
    public String getDescription() {
        return "Clear all tasks";
    }
    
    @Override
    public String getUsage() {
        return "clear";
    }
    
    @Override
    public String getCategory() {
        return "UTILITY";
    }
}