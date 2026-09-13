package tasktracker.ui.commands;

import tasktracker.service.TaskService;

public class ExitCommand implements ICommand {
    
    @Override
    public void execute(String[] args, TaskService service) {
        System.out.println("Goodbye!");
        System.exit(0);
    }
    
    @Override
    public String getDescription() {
        return "Exit the application";
    }
    
    @Override
    public String getUsage() {
        return "exit";
    }
    
    @Override
    public String getCategory() {
        return "UTILITY";
    }
}