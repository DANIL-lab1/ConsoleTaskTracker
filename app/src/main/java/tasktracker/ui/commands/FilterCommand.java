package tasktracker.ui.commands;

import tasktracker.model.Status;
import tasktracker.model.Task;
import tasktracker.service.TaskService;
import tasktracker.ui.ArgumentParser;
import tasktracker.ui.TablePrinter;
import java.util.List;
import java.util.Map;

public class FilterCommand implements ICommand {
    
    @Override
    public void execute(String[] args, TaskService service) {
        Map<String, String> parsedArgs = ArgumentParser.parseArguments(args);
        String statusStr = ArgumentParser.getValue(parsedArgs, "status");
        
        if (statusStr == null || statusStr.isEmpty()) {
            System.out.println("  Error: Status required. Use --status NEW|IN_PROGRESS|DONE");
            System.out.println("Usage: " + getUsage());
            return;
        }
        
        Status status;
        try {
            status = Status.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("  Error: Invalid status. Use: NEW, IN_PROGRESS, or DONE");
            return;
        }
        
        List<Task> tasks = service.getTasksByStatus(status);
        
        if (tasks.isEmpty()) {
            System.out.println("  No tasks with status: " + status);
            return;
        }
        
        System.out.println("  Tasks with status '" + status + "' (" + tasks.size() + "):");
        TablePrinter.printTasks(tasks);
    }
    
    @Override
    public String getDescription() {
        return "Filter tasks by status";
    }
    
    @Override
    public String getUsage() {
        return "filter --status NEW|IN_PROGRESS|DONE";
    }
}